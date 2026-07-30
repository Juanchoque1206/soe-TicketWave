package com.soe.jcb.eventdriven.demo.order.dto;

import com.soe.jcb.eventdriven.demo.order.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        String ticketTypeName,
        BigDecimal unitPrice,
        Long seatId,
        String seatRow,
        Integer seatNumber
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getTicketType().getName(),
                item.getUnitPrice(),
                item.getSeat() != null ? item.getSeat().getId() : null,
                item.getSeat() != null ? item.getSeat().getRow() : null,
                item.getSeat() != null ? item.getSeat().getNumber() : null
        );
    }
}
