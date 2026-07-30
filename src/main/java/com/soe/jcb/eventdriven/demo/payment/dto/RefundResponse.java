package com.soe.jcb.eventdriven.demo.payment.dto;

import com.soe.jcb.eventdriven.demo.payment.entity.PaymentStatus;

import java.math.BigDecimal;

public record RefundResponse(
        PaymentStatus status,
        BigDecimal refundedAmount,
        String message
) {
}
