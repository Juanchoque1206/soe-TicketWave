package com.soe.jcb.eventdriven.demo.common.exception;

public class InsufficientTicketsException extends RuntimeException {

    private final Long ticketTypeId;
    private final int requested;
    private final int available;

    public InsufficientTicketsException(Long ticketTypeId, int requested, int available) {
        super(String.format("Insufficient tickets for ticket type %d: requested %d, available %d",
                ticketTypeId, requested, available));
        this.ticketTypeId = ticketTypeId;
        this.requested = requested;
        this.available = available;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public int getRequested() {
        return requested;
    }

    public int getAvailable() {
        return available;
    }
}
