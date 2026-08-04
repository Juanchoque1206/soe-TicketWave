package com.soe.jcb.eventdriven.demo.payment.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Payment entity (pure domain - no annotations).
 */
public class Payment {

    private Long id;
    private Long orderId;
    private String externalTransactionId;
    private BigDecimal amount;
    private PaymentMethod method;
    private PaymentStatus status = PaymentStatus.PENDING;
    private LocalDateTime paidAt;
    private LocalDateTime refundedAt;

    public Payment(Long id, Long orderId, String externalTransactionId, BigDecimal amount,
                   PaymentMethod method, PaymentStatus status, LocalDateTime paidAt,
                   LocalDateTime refundedAt) {
        this.id = id;
        this.orderId = orderId;
        this.externalTransactionId = externalTransactionId;
        this.amount = amount;
        this.method = method;
        this.status = status;
        this.paidAt = paidAt;
        this.refundedAt = refundedAt;
    }

    public void markProcessed(String transactionId, LocalDateTime now) {
        this.externalTransactionId = transactionId;
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = now;
    }

    public void markRefunded(LocalDateTime now) {
        this.status = PaymentStatus.REFUNDED;
        this.refundedAt = now;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getExternalTransactionId() {
        return externalTransactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public PaymentStatus getStatus() {
        return status;
    }

    public LocalDateTime getPaidAt() {
        return paidAt;
    }

    public LocalDateTime getRefundedAt() {
        return refundedAt;
    }
}