package com.ticketwave.application.dto;

import com.ticketwave.domain.payment.model.PaymentMethod;
import com.ticketwave.domain.payment.model.PaymentStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(
        Long id,
        Long orderId,
        String orderNumber,
        BigDecimal amount,
        PaymentMethod method,
        PaymentStatus status,
        String externalTransactionId,
        LocalDateTime paidAt
) implements Serializable {
}
