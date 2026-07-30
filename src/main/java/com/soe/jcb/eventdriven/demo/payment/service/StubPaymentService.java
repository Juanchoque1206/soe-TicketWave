package com.soe.jcb.eventdriven.demo.payment.service;

import com.soe.jcb.eventdriven.demo.common.event.DomainEventBus;
import com.soe.jcb.eventdriven.demo.payment.dto.PaymentRequest;
import com.soe.jcb.eventdriven.demo.payment.dto.PaymentResponse;
import com.soe.jcb.eventdriven.demo.payment.service.PaymentService;
import com.soe.jcb.eventdriven.demo.payment.event.PaymentCompletedEvent;
import com.soe.jcb.eventdriven.demo.payment.event.PaymentRefundedEvent;
import com.soe.jcb.eventdriven.demo.payment.dto.RefundRequest;
import com.soe.jcb.eventdriven.demo.payment.dto.RefundResponse;
import com.soe.jcb.eventdriven.demo.order.entity.Order;
import com.soe.jcb.eventdriven.demo.order.entity.OrderStatus;
import com.soe.jcb.eventdriven.demo.payment.entity.Payment;
import com.soe.jcb.eventdriven.demo.payment.entity.PaymentStatus;
import com.soe.jcb.eventdriven.demo.common.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.common.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.order.repository.OrderRepository;
import com.soe.jcb.eventdriven.demo.payment.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class StubPaymentService implements PaymentService {

    private static final Logger log = LoggerFactory.getLogger(StubPaymentService.class);

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final DomainEventBus eventBus;

    public StubPaymentService(OrderRepository orderRepository,
                               PaymentRepository paymentRepository,
                               DomainEventBus eventBus) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.eventBus = eventBus;
    }

    @Override
    @Transactional
    public PaymentResponse processPayment(String orderNumber, PaymentRequest request) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Order is not in PENDING status");
        }

        paymentRepository.findByOrderId(order.getId()).ifPresent(existing -> {
            throw new BusinessRuleException("Payment already exists for this order");
        });

        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setAmount(order.getTotalAmount());
        payment.setMethod(request.method());
        payment.setStatus(PaymentStatus.COMPLETED);
        payment.setExternalTransactionId("STUB-" + UUID.randomUUID());
        payment.setPaidAt(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        eventBus.publish(new PaymentCompletedEvent(
                payment.getId(), order.getId(), orderNumber, order.getUser().getId(), payment.getAmount()));

        log.info("Stub payment processed for order {}: amount={}, transactionId={}",
                orderNumber, payment.getAmount(), payment.getExternalTransactionId());

        return PaymentResponse.from(payment);
    }

    @Override
    @Transactional
    public RefundResponse processRefund(String orderNumber, RefundRequest request) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        Payment payment = paymentRepository.findByOrderId(order.getId())
                .orElseThrow(() -> new BusinessRuleException("No payment found for this order"));

        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BusinessRuleException("Payment is not in COMPLETED status");
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        payment.setRefundedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        eventBus.publish(new PaymentRefundedEvent(
                payment.getId(), order.getId(), orderNumber, payment.getAmount()));

        log.info("Stub refund processed for order {}: amount={}, reason={}",
                orderNumber, payment.getAmount(), request.reason());

        return new RefundResponse(PaymentStatus.REFUNDED, payment.getAmount(), "Refund processed successfully");
    }
}
