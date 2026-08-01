package com.ticketwave.application.mapper;

import com.ticketwave.application.dto.OrderItemResponse;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toResponse(Order order) {
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        return new OrderResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getUserId(),
                order.getStatus(),
                order.getEventId(),
                order.getEventName(),
                order.getEventDate(),
                order.getVenueName(),
                order.getTotalAmount(),
                order.getPromotionCode(),
                order.getDiscountAmount(),
                order.getCreatedAt(),
                items);
    }

    public OrderItemResponse toItemResponse(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getTicketTypeId(),
                item.getTicketTypeName(),
                item.getUnitPrice(),
                item.getSeatId(),
                item.getSeatRow(),
                item.getSeatNumber());
    }
}
