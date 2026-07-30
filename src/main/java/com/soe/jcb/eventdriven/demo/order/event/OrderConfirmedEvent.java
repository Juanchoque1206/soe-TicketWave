package com.soe.jcb.eventdriven.demo.order.event;

import com.soe.jcb.eventdriven.demo.common.event.AbstractDomainEvent;

import java.io.Serial;

public class OrderConfirmedEvent extends AbstractDomainEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long orderId;
    private final String orderNumber;
    private final Long userId;

    public OrderConfirmedEvent(Long orderId, String orderNumber, Long userId) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
    }

    public Long getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public Long getUserId() { return userId; }

    @Override
    public String getRoutingKey() {
        return "order.confirmed";
    }
}
