package com.soe.jcb.eventdriven.demo.ticket.event;

import com.soe.jcb.eventdriven.demo.common.event.AbstractDomainEvent;

public class TicketCompensationRequestedEvent extends AbstractDomainEvent {

    private final Long orderId;
    private final String orderNumber;

    public TicketCompensationRequestedEvent(Long orderId, String orderNumber) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
    }

    public Long getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }

    @Override
    public String getRoutingKey() {
        return "ticket.compensation.requested";
    }
}
