package com.soe.jcb.eventdriven.demo.saga.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Saga event transport DTO used across context boundaries over the message
 * broker. Lives in the shared saga package so producers/consumers agree on
 * the payload shape without depending on a specific context's DTOs.
 */
public record OrderEvent(
        String type,
        Long orderId,
        String orderNumber,
        Long userId,
        Long eventId,
        BigDecimal totalAmount,
        LocalDateTime createdAt,
        List<Item> items
) {
    public record Item(Long ticketTypeId, Long seatId, String seatRow, Integer seatNumber) {
    }

    public static OrderEvent created(Long orderId, String orderNumber, Long userId) {
        return new OrderEvent("ORDER_CREATED", orderId, orderNumber, userId, null, null, null, List.of());
    }

    public static OrderEvent confirmed(Long orderId, String orderNumber, Long userId) {
        return new OrderEvent("ORDER_CONFIRMED", orderId, orderNumber, userId, null, null, null, List.of());
    }

    public static OrderEvent cancelled(Long orderId, String orderNumber, Long userId) {
        return new OrderEvent("ORDER_CANCELLED", orderId, orderNumber, userId, null, null, null, List.of());
    }
}