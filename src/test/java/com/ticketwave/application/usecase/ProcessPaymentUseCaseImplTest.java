package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.PaymentRequest;
import com.ticketwave.application.dto.PaymentResponse;
import com.ticketwave.application.mapper.PaymentMapper;
import com.ticketwave.domain.payment.event.PaymentCompletedEvent;
import com.ticketwave.domain.payment.model.OrderInfo;
import com.ticketwave.domain.payment.model.Payment;
import com.ticketwave.domain.payment.model.PaymentMethod;
import com.ticketwave.domain.payment.model.PaymentStatus;
import com.ticketwave.domain.payment.repository.OrderQueryPort;
import com.ticketwave.domain.payment.repository.PaymentGateway;
import com.ticketwave.domain.payment.repository.PaymentRepository;
import com.ticketwave.domain.payment.repository.PaymentResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProcessPaymentUseCaseImplTest {

    @Mock
    private OrderQueryPort orderQueryPort;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentGateway paymentGateway;
    @Mock
    private DomainEventBus eventBus;

    private ProcessPaymentUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ProcessPaymentUseCaseImpl(
                orderQueryPort, paymentRepository, paymentGateway, eventBus, new PaymentMapper());
    }

    @Test
    void process_completesPaymentAndPublishesCompletedEvent() {
        when(orderQueryPort.findByOrderNumber("ORD-1"))
                .thenReturn(Optional.of(new OrderInfo(10L, "ORD-1", 7L, new BigDecimal("100.00"), "PENDING")));
        when(paymentRepository.findByOrderId(10L)).thenReturn(Optional.empty());
        when(paymentGateway.charge(10L, "ORD-1", new BigDecimal("100.00"), PaymentMethod.CREDIT_CARD))
                .thenReturn(PaymentResult.success("TXN-1"));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(inv -> {
            Payment payment = inv.getArgument(0);
            payment.setId(20L);
            return payment;
        });

        PaymentResponse response = useCase.process("ORD-1", new PaymentRequest(PaymentMethod.CREDIT_CARD));

        assertThat(response.status()).isEqualTo(PaymentStatus.COMPLETED);
        assertThat(response.externalTransactionId()).isEqualTo("TXN-1");

        ArgumentCaptor<PaymentCompletedEvent> captor = ArgumentCaptor.forClass(PaymentCompletedEvent.class);
        verify(eventBus).publish(captor.capture());
        assertThat(captor.getValue().getPaymentId()).isEqualTo(20L);
        assertThat(captor.getValue().getOrderId()).isEqualTo(10L);
        assertThat(captor.getValue().getOrderNumber()).isEqualTo("ORD-1");
        assertThat(captor.getValue().getUserId()).isEqualTo(7L);
        assertThat(captor.getValue().getAmount()).isEqualByComparingTo("100.00");
    }

    @Test
    void process_rejectsNonPendingOrder() {
        when(orderQueryPort.findByOrderNumber("ORD-1"))
                .thenReturn(Optional.of(new OrderInfo(10L, "ORD-1", 7L, new BigDecimal("100.00"), "CONFIRMED")));

        assertThatThrownBy(() -> useCase.process("ORD-1", new PaymentRequest(PaymentMethod.CREDIT_CARD)))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("not in PENDING status");
    }
}
