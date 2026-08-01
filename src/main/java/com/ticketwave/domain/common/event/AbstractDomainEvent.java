package com.ticketwave.domain.common.event;

import java.io.Serial;
import java.time.LocalDateTime;
import java.util.UUID;

public abstract class AbstractDomainEvent implements DomainEvent {

    @Serial
    private static final long serialVersionUID = 1L;

    private final UUID eventId;
    private final LocalDateTime occurredOn;

    protected AbstractDomainEvent() {
        this.eventId = UUID.randomUUID();
        this.occurredOn = LocalDateTime.now();
    }

    @Override
    public UUID eventId() {
        return eventId;
    }

    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }
}
