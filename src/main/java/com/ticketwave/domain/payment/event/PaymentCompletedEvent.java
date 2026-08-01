package com.ticketwave.domain.payment.event;

import com.ticketwave.domain.common.event.AbstractDomainEvent;

import java.io.Serial;
import java.math.BigDecimal;

public class PaymentCompletedEvent extends AbstractDomainEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long paymentId;
    private final Long orderId;
    private final String orderNumber;
    private final Long userId;
    private final BigDecimal amount;

    public PaymentCompletedEvent(Long paymentId, Long orderId, String orderNumber, Long userId, BigDecimal amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.amount = amount;
    }

    public Long getPaymentId() {
        return paymentId;
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

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String getRoutingKey() {
        return "payment.completed";
    }
}
