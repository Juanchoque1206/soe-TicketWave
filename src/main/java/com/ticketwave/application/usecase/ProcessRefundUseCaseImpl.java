package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.RefundRequest;
import com.ticketwave.application.dto.RefundResponse;
import com.ticketwave.domain.payment.event.PaymentRefundedEvent;
import com.ticketwave.domain.payment.model.OrderInfo;
import com.ticketwave.domain.payment.model.Payment;
import com.ticketwave.domain.payment.model.PaymentStatus;
import com.ticketwave.application.usecase.ProcessRefundUseCase;
import com.ticketwave.domain.payment.repository.OrderQueryPort;
import com.ticketwave.domain.payment.repository.PaymentGateway;
import com.ticketwave.domain.payment.repository.PaymentRepository;
import com.ticketwave.domain.payment.repository.PaymentResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProcessRefundUseCaseImpl implements ProcessRefundUseCase {

    private final OrderQueryPort orderQueryPort;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final DomainEventBus eventBus;

    public ProcessRefundUseCaseImpl(OrderQueryPort orderQueryPort,
                                    PaymentRepository paymentRepository,
                                    PaymentGateway paymentGateway,
                                    DomainEventBus eventBus) {
        this.orderQueryPort = orderQueryPort;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public RefundResponse refund(String orderNumber, RefundRequest request) {
        OrderInfo order = orderQueryPort.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        Payment payment = paymentRepository.findByOrderId(order.orderId())
                .orElseThrow(() -> new BusinessRuleException("No payment found for this order"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BusinessRuleException("Payment is not in COMPLETED status");
        }

        PaymentResult result = paymentGateway.refund(payment.getId(), orderNumber, payment.getAmount());
        if (!result.success()) {
            throw new BusinessRuleException("Refund failed: " + result.errorMessage());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        eventBus.publish(new PaymentRefundedEvent(
                payment.getId(), order.orderId(), orderNumber, payment.getAmount()));

        return new RefundResponse(PaymentStatus.REFUNDED, payment.getAmount(), "Refund processed successfully");
    }
}
