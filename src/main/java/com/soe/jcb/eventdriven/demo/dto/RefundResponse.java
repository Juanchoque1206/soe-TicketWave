package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.PaymentStatus;

import java.math.BigDecimal;

public record RefundResponse(
        PaymentStatus status,
        BigDecimal refundedAmount,
        String message
) {
}
