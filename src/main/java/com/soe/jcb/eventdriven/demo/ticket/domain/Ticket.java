package com.soe.jcb.eventdriven.demo.ticket.domain;

import java.time.LocalDateTime;

/**
 * Ticket entity (pure domain - no annotations).
 *
 * <p>References the order item by id (orderItemId) to avoid a cross-context
 * object reference to the order context.
 */
public class Ticket {

    private Long id;
    private String ticketCode;
    private Long orderItemId;
    private TicketStatus status = TicketStatus.VALID;
    private LocalDateTime issuedAt;
    private LocalDateTime validatedAt;

    public Ticket(Long id, String ticketCode, Long orderItemId, TicketStatus status,
                  LocalDateTime issuedAt, LocalDateTime validatedAt) {
        this.id = id;
        this.ticketCode = ticketCode;
        this.orderItemId = orderItemId;
        this.status = status;
        this.issuedAt = issuedAt;
        this.validatedAt = validatedAt;
    }

    public void validate(LocalDateTime now) {
        if (status != TicketStatus.VALID) {
            throw new IllegalStateException("Ticket is not valid. Current status: " + status);
        }
        status = TicketStatus.USED;
        validatedAt = now;
    }

    public Long getId() {
        return id;
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }
}