package com.ticketwave.domain.payment.event;

import com.ticketwave.domain.common.event.AbstractDomainEvent;

import java.io.Serial;
import java.math.BigDecimal;

public class PaymentRefundedEvent extends AbstractDomainEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final Long paymentId;
    private final Long orderId;
    private final String orderNumber;
    private final BigDecimal amount;

    public PaymentRefundedEvent(Long paymentId, Long orderId, String orderNumber, BigDecimal amount) {
        this.paymentId = paymentId;
        this.orderId = orderId;
        this.orderNumber = orderNumber;
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

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String getRoutingKey() {
        return "payment.refunded";
    }
}
