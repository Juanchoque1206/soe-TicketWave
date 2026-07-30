package com.soe.jcb.eventdriven.demo.service;

import com.soe.jcb.eventdriven.demo.dto.PaymentRequest;
import com.soe.jcb.eventdriven.demo.dto.PaymentResponse;
import com.soe.jcb.eventdriven.demo.dto.RefundRequest;
import com.soe.jcb.eventdriven.demo.dto.RefundResponse;
import com.soe.jcb.eventdriven.demo.entity.Order;
import com.soe.jcb.eventdriven.demo.entity.OrderStatus;
import com.soe.jcb.eventdriven.demo.entity.Payment;
import com.soe.jcb.eventdriven.demo.entity.PaymentStatus;
import com.soe.jcb.eventdriven.demo.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.repository.OrderRepository;
import com.soe.jcb.eventdriven.demo.repository.PaymentRepository;
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
    private final OrderService orderService;

    public StubPaymentService(OrderRepository orderRepository,
                               PaymentRepository paymentRepository,
                               OrderService orderService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
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

        orderService.confirmOrder(order.getId());

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

        log.info("Stub refund processed for order {}: amount={}, reason={}",
                orderNumber, payment.getAmount(), request.reason());

        return new RefundResponse(PaymentStatus.REFUNDED, payment.getAmount(), "Refund processed successfully");
    }
}
