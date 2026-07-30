package com.soe.jcb.eventdriven.demo.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "eventbus.type", havingValue = "inmemory", matchIfMissing = true)
public class InMemoryEventBus implements DomainEventBus {

    private static final Logger log = LoggerFactory.getLogger(InMemoryEventBus.class);

    private final ApplicationEventPublisher publisher;

    public InMemoryEventBus(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }

    @Override
    public void publish(DomainEvent event) {
        log.debug("Publishing in-memory event: {} (routingKey={})",
                event.getClass().getSimpleName(), event.getRoutingKey());
        publisher.publishEvent(event);
    }
}
