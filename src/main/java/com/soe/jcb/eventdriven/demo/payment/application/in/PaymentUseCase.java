package com.soe.jcb.eventdriven.demo.payment.application.in;

import com.soe.jcb.eventdriven.demo.payment.domain.PaymentMethod;
import com.soe.jcb.eventdriven.demo.payment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Use-case boundary for payment processing (pure, no Spring).
 */
public interface PaymentUseCase {

    PaymentResult processPayment(String orderNumber, ProcessPaymentCommand command);

    RefundResult processRefund(String orderNumber, RefundCommand command);

    record ProcessPaymentCommand(PaymentMethod method) {
    }

    record RefundCommand(String reason) {
    }

    record PaymentResult(Long id, Long orderId, String externalTransactionId, BigDecimal amount,
                         PaymentMethod method, PaymentStatus status, LocalDateTime paidAt) {
    }

    record RefundResult(PaymentStatus status, BigDecimal amount, String message) {
    }
}