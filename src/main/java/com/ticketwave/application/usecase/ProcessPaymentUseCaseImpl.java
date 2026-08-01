package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.PaymentRequest;
import com.ticketwave.application.dto.PaymentResponse;
import com.ticketwave.application.mapper.PaymentMapper;
import com.ticketwave.domain.payment.event.PaymentCompletedEvent;
import com.ticketwave.domain.payment.model.OrderInfo;
import com.ticketwave.domain.payment.model.Payment;
import com.ticketwave.domain.payment.model.PaymentStatus;
import com.ticketwave.application.usecase.ProcessPaymentUseCase;
import com.ticketwave.domain.payment.repository.OrderQueryPort;
import com.ticketwave.domain.payment.repository.PaymentGateway;
import com.ticketwave.domain.payment.repository.PaymentRepository;
import com.ticketwave.domain.payment.repository.PaymentResult;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProcessPaymentUseCaseImpl implements ProcessPaymentUseCase {

    private static final String ORDER_PENDING = "PENDING";

    private final OrderQueryPort orderQueryPort;
    private final PaymentRepository paymentRepository;
    private final PaymentGateway paymentGateway;
    private final DomainEventBus eventBus;
    private final PaymentMapper paymentMapper;

    public ProcessPaymentUseCaseImpl(OrderQueryPort orderQueryPort,
                                     PaymentRepository paymentRepository,
                                     PaymentGateway paymentGateway,
                                     DomainEventBus eventBus,
                                     PaymentMapper paymentMapper) {
        this.orderQueryPort = orderQueryPort;
        this.paymentRepository = paymentRepository;
        this.paymentGateway = paymentGateway;
        this.eventBus = eventBus;
        this.paymentMapper = paymentMapper;
    }

    @Override
    @Transactional
    public PaymentResponse process(String orderNumber, PaymentRequest request) {
        OrderInfo order = orderQueryPort.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (!ORDER_PENDING.equals(order.status())) {
            throw new BusinessRuleException("Order is not in PENDING status");
        }

        paymentRepository.findByOrderId(order.orderId()).ifPresent(existing -> {
            throw new BusinessRuleException("Payment already exists for this order");
        });

        PaymentResult result = paymentGateway.charge(order.orderId(), orderNumber, order.totalAmount(), request.method());
        if (!result.success()) {
            throw new BusinessRuleException("Payment failed: " + result.errorMessage());
        }

        Payment payment = new Payment();
        payment.setOrderId(order.orderId());
        payment.setOrderNumber(orderNumber);
        payment.setUserId(order.userId());
        payment.setAmount(order.totalAmount());
        payment.setMethod(request.method());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setExternalTransactionId(result.transactionId());
        payment.setPaidAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        eventBus.publish(new PaymentCompletedEvent(
                payment.getId(), order.orderId(), orderNumber, order.userId(), payment.getAmount()));

        return paymentMapper.toResponse(payment);
    }
}
