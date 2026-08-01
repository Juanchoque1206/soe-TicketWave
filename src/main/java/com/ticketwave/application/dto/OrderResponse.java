package com.ticketwave.application.dto;

import com.ticketwave.domain.ticket.model.OrderStatus;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        Long userId,
        OrderStatus status,
        Long eventId,
        String eventName,
        LocalDateTime eventDate,
        String venueName,
        BigDecimal totalAmount,
        String promotionCode,
        BigDecimal discountAmount,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) implements Serializable {
}
