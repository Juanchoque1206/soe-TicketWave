package com.ticketwave.infrastructure.eventbus;

import com.ticketwave.domain.common.event.DomainEvent;

public interface DomainEventBus {

    void publish(DomainEvent event);
}
