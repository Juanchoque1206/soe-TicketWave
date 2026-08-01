package com.ticketwave.application.dto;

import com.ticketwave.domain.payment.model.PaymentStatus;

import java.io.Serializable;
import java.math.BigDecimal;

public record RefundResponse(
        PaymentStatus status,
        BigDecimal refundedAmount,
        String message
) implements Serializable {
}
