package com.soe.jcb.eventdriven.demo.payment.interfaces.dto;

import com.soe.jcb.eventdriven.demo.payment.domain.PaymentStatus;

import java.math.BigDecimal;

public record RefundResponse(
        PaymentStatus status,
        BigDecimal amount,
        String message
) {
    public static RefundResponse from(com.soe.jcb.eventdriven.demo.payment.application.in.PaymentUseCase.RefundResult result) {
        return new RefundResponse(result.status(), result.amount(), result.message());
    }
}