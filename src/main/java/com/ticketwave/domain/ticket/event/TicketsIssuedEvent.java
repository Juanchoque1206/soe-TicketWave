package com.ticketwave.domain.ticket.event;

import com.ticketwave.domain.common.event.AbstractDomainEvent;

import java.io.Serial;
import java.util.List;

public class TicketsIssuedEvent extends AbstractDomainEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long orderId;
    private final String orderNumber;
    private final Long userId;
    private final List<Long> ticketIds;

    public TicketsIssuedEvent(Long orderId, String orderNumber, Long userId, List<Long> ticketIds) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.ticketIds = ticketIds;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public List<Long> getTicketIds() {
        return ticketIds;
    }

    @Override
    public String getRoutingKey() {
        return "ticket.issued";
    }
}
