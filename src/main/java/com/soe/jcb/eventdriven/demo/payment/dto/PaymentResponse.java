package com.soe.jcb.eventdriven.demo.payment.dto;

import com.soe.jcb.eventdriven.demo.payment.entity.Payment;
import com.soe.jcb.eventdriven.demo.payment.entity.PaymentMethod;
import com.soe.jcb.eventdriven.demo.payment.entity.PaymentStatus;

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
) {
    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getOrder().getOrderNumber(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getExternalTransactionId(),
                payment.getPaidAt()
        );
    }
}
