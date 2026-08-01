package com.ticketwave.domain.payment.model;

import java.math.BigDecimal;

public record OrderInfo(
        Long orderId,
        String orderNumber,
        Long userId,
        BigDecimal totalAmount,
        String status
) {
}
