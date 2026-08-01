package com.ticketwave.domain.ticket.event;

import com.ticketwave.domain.common.event.AbstractDomainEvent;

import java.io.Serial;

public class TicketValidatedEvent extends AbstractDomainEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long ticketId;
    private final String ticketCode;
    private final Long orderId;

    public TicketValidatedEvent(Long ticketId, String ticketCode, Long orderId) {
        this.ticketId = ticketId;
        this.ticketCode = ticketCode;
        this.orderId = orderId;
    }

    public Long getTicketId() {
        return ticketId;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public Long getOrderId() {
        return orderId;
    }

    @Override
    public String getRoutingKey() {
        return "ticket.validated";
    }
}
