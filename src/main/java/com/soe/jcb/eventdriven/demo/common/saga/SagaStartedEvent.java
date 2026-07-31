package com.soe.jcb.eventdriven.demo.common.saga;

import com.soe.jcb.eventdriven.demo.common.event.AbstractDomainEvent;

public class SagaStartedEvent extends AbstractDomainEvent {

    private final Long orderId;
    private final String orderNumber;
    private final Long userId;

    public SagaStartedEvent(Long orderId, String orderNumber, Long userId) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
    }

    public Long getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public Long getUserId() { return userId; }

    @Override
    public String getRoutingKey() {
        return "saga.started";
    }
}
