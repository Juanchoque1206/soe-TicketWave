package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.event.infrastructure.persistence.TicketTypeJpaEntity;
import com.soe.jcb.eventdriven.demo.event.infrastructure.persistence.TicketTypeJpaRepository;
import com.soe.jcb.eventdriven.demo.order.application.out.TicketTypeOrderPort;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adapter bridging the order context's {@link TicketTypeOrderPort} to the event
 * context's ticket-type inventory (cross-context at infrastructure layer only).
 */
@Component
public class TicketTypeOrderPortAdapter implements TicketTypeOrderPort {

    private final TicketTypeJpaRepository jpaRepository;

    public TicketTypeOrderPortAdapter(TicketTypeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TicketTypeSnapshot> findById(Long id) {
        return jpaRepository.findById(id)
                .map(tt -> new TicketTypeSnapshot(tt.getId(), tt.getEventId(), tt.getName(), tt.getPrice()));
    }

    @Override
    public boolean belongsToEvent(Long ticketTypeId, Long eventId) {
        return jpaRepository.findById(ticketTypeId)
                .map(TicketTypeJpaEntity::getEventId)
                .filter(eventId::equals)
                .isPresent();
    }

    @Override
    @Transactional
    public boolean reserve(Long id, int qty) {
        return jpaRepository.reserve(id, qty) > 0;
    }

    @Override
    @Transactional
    public boolean release(Long id, int qty) {
        return jpaRepository.release(id, qty) > 0;
    }
}