package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.event.infrastructure.persistence.EventJpaRepository;
import com.soe.jcb.eventdriven.demo.event.infrastructure.persistence.EventJpaEntity;
import com.soe.jcb.eventdriven.demo.order.application.out.EventOrderPort;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter bridging the order context's {@link EventOrderPort} to the event
 * context's JPA persistence (cross-context at the infrastructure layer only).
 */
@Component
public class EventOrderPortAdapter implements EventOrderPort {

    private final EventJpaRepository eventJpaRepository;

    public EventOrderPortAdapter(EventJpaRepository eventJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
    }

    @Override
    public Optional<EventSnapshot> findById(Long eventId) {
        return eventJpaRepository.findById(eventId)
                .map(e -> new EventSnapshot(e.getId(), e.getStatus().name(),
                        e.getSalesStartDate(), e.getSalesEndDate()));
    }
}