package com.soe.jcb.eventdriven.demo.common.saga;

import com.soe.jcb.eventdriven.demo.common.event.AbstractDomainEvent;

public class SagaFailedEvent extends AbstractDomainEvent {

    private final Long orderId;
    private final String orderNumber;
    private final Long userId;
    private final String reason;
    private final String failedStep;

    public SagaFailedEvent(Long orderId, String orderNumber, Long userId, String reason, String failedStep) {
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.reason = reason;
        this.failedStep = failedStep;
    }

    public Long getOrderId() { return orderId; }
    public String getOrderNumber() { return orderNumber; }
    public Long getUserId() { return userId; }
    public String getReason() { return reason; }
    public String getFailedStep() { return failedStep; }

    @Override
    public String getRoutingKey() {
        return "saga.failed";
    }
}
