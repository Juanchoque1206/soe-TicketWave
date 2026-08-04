package com.soe.jcb.eventdriven.demo.payment.application.service;

import com.soe.jcb.eventdriven.demo.common.domain.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.common.domain.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.payment.application.in.PaymentUseCase;
import com.soe.jcb.eventdriven.demo.payment.application.out.OrderPaymentPort;
import com.soe.jcb.eventdriven.demo.payment.application.out.PaymentGatewayPort;
import com.soe.jcb.eventdriven.demo.payment.domain.Payment;
import com.soe.jcb.eventdriven.demo.payment.domain.PaymentRepository;
import com.soe.jcb.eventdriven.demo.payment.domain.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PaymentService implements PaymentUseCase {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final OrderPaymentPort orderPaymentPort;
    private final PaymentRepository paymentRepository;
    private final PaymentGatewayPort paymentGatewayPort;

    public PaymentService(OrderPaymentPort orderPaymentPort,
                          PaymentRepository paymentRepository,
                          PaymentGatewayPort paymentGatewayPort) {
        this.orderPaymentPort = orderPaymentPort;
        this.paymentRepository = paymentRepository;
        this.paymentGatewayPort = paymentGatewayPort;
    }

    @Override
    @Transactional
    public PaymentResult processPayment(String orderNumber, ProcessPaymentCommand command) {
        OrderPaymentPort.OrderSnapshot order = orderPaymentPort.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderNumber", orderNumber);
        }
        if (!"PENDING".equals(order.status())) {
            throw new BusinessRuleException("Order is not in PENDING status");
        }
        paymentRepository.findByOrderId(order.id()).ifPresent(existing -> {
            throw new BusinessRuleException("Payment already exists for this order");
        });

        PaymentGatewayPort.PaymentGatewayResult gatewayResult = paymentGatewayPort.charge(order.totalAmount(), command.method().name());

        Payment payment = new Payment(null, order.id(), null, order.totalAmount(),
                command.method(), PaymentStatus.FAILED, null, null);
        if (gatewayResult.success()) {
            payment.markProcessed(gatewayResult.externalTransactionId(), LocalDateTime.now());
            orderPaymentPort.confirmOrder(order.id());
        }

        Payment saved = paymentRepository.save(payment);
        log.info("Payment processed for order {}: status={}, transactionId={}",
                orderNumber, saved.getStatus(), saved.getExternalTransactionId());
        return toResult(saved);
    }

    @Override
    @Transactional
    public RefundResult processRefund(String orderNumber, RefundCommand command) {
        OrderPaymentPort.OrderSnapshot order = orderPaymentPort.findByOrderNumber(orderNumber);
        if (order == null) {
            throw new ResourceNotFoundException("Order", "orderNumber", orderNumber);
        }

        Payment payment = paymentRepository.findByOrderId(order.id())
                .orElseThrow(() -> new BusinessRuleException("No payment found for this order"));
        if (payment.getStatus() != PaymentStatus.COMPLETED) {
            throw new BusinessRuleException("Payment is not in COMPLETED status");
        }

        payment.markRefunded(LocalDateTime.now());
        paymentRepository.save(payment);

        log.info("Refund processed for order {}: amount={}, reason={}",
                orderNumber, payment.getAmount(), command.reason());
        return new RefundResult(PaymentStatus.REFUNDED, payment.getAmount(), "Refund processed successfully");
    }

    private PaymentResult toResult(Payment payment) {
        return new PaymentResult(payment.getId(), payment.getOrderId(), payment.getExternalTransactionId(),
                payment.getAmount(), payment.getMethod(), payment.getStatus(), payment.getPaidAt());
    }
}