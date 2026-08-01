package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderItem;
import com.ticketwave.domain.ticket.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketPersistenceMapper {

    public Order toDomain(JpaOrder jpa) {
        Order order = new Order();
        order.setId(jpa.getId());
        order.setOrderNumber(jpa.getOrderNumber());
        order.setUserId(jpa.getUserId());
        order.setStatus(jpa.getStatus());
        order.setEventId(jpa.getEventId());
        order.setEventName(jpa.getEventName());
        order.setEventDate(jpa.getEventDate());
        order.setVenueName(jpa.getVenueName());
        order.setTotalAmount(jpa.getTotalAmount());
        order.setPromotionCode(jpa.getPromotionCode());
        order.setDiscountAmount(jpa.getDiscountAmount());
        order.setCreatedAt(jpa.getCreatedAt());
        order.setUpdatedAt(jpa.getUpdatedAt());
        if (jpa.getItems() != null) {
            order.setItems(jpa.getItems().stream().map(this::toDomain).toList());
        }
        return order;
    }

    public OrderItem toDomain(JpaOrderItem jpa) {
        OrderItem item = new OrderItem();
        item.setId(jpa.getId());
        item.setTicketTypeId(jpa.getTicketTypeId());
        item.setTicketTypeName(jpa.getTicketTypeName());
        item.setUnitPrice(jpa.getUnitPrice());
        item.setSeatId(jpa.getSeatId());
        item.setSeatRow(jpa.getSeatRow());
        item.setSeatNumber(jpa.getSeatNumber());
        item.setCreatedAt(jpa.getCreatedAt());
        item.setUpdatedAt(jpa.getUpdatedAt());
        return item;
    }

    public Ticket toDomain(JpaTicket jpa) {
        Ticket ticket = new Ticket();
        ticket.setId(jpa.getId());
        ticket.setTicketCode(jpa.getTicketCode());
        ticket.setOrderId(jpa.getOrderId());
        ticket.setOrderItemId(jpa.getOrderItemId());
        ticket.setStatus(jpa.getStatus());
        ticket.setIssuedAt(jpa.getIssuedAt());
        ticket.setValidatedAt(jpa.getValidatedAt());
        ticket.setEventName(jpa.getEventName());
        ticket.setEventDate(jpa.getEventDate());
        ticket.setVenueName(jpa.getVenueName());
        ticket.setTicketTypeName(jpa.getTicketTypeName());
        ticket.setSeatRow(jpa.getSeatRow());
        ticket.setSeatNumber(jpa.getSeatNumber());
        ticket.setCreatedAt(jpa.getCreatedAt());
        ticket.setUpdatedAt(jpa.getUpdatedAt());
        return ticket;
    }
}
