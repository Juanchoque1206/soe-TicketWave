package com.soe.jcb.eventdriven.demo.payment.interfaces.dto;

import com.soe.jcb.eventdriven.demo.payment.domain.PaymentMethod;
import com.soe.jcb.eventdriven.demo.payment.domain.PaymentStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        String externalTransactionId,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        LocalDateTime paidAt
) {
    public static PaymentResponse from(com.soe.jcb.eventdriven.demo.payment.application.in.PaymentUseCase.PaymentResult result) {
        return new PaymentResponse(result.id(), result.orderId(), result.externalTransactionId(),
                result.amount(), result.method(), result.status(), result.paidAt());
    }
}