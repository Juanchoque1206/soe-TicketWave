package com.soe.jcb.eventdriven.demo.common.saga;

public enum SagaStatus {
    CREATED,
    ORDER_SUBMITTED,
    PAYMENT_COMPLETED,
    ORDER_CONFIRMED,
    TICKETS_ISSUED,
    COMPLETED,
    FAILED,
    COMPENSATING,
    COMPENSATED
}
