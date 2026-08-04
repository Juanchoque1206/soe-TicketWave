package com.soe.jcb.eventdriven.demo.event.domain;

import java.util.List;
import java.util.Optional;

/**
 * Domain port for persisting and reserving {@link TicketType}.
 */
public interface TicketTypeRepository {

    Optional<TicketType> findById(Long id);

    List<TicketType> findByEventId(Long eventId);

    /**
     * Atomically increments soldQuantity, returning true only if inventory allows.
     */
    boolean reserve(Long id, int qty);

    /**
     * Atomically decrements soldQuantity.
     */
    boolean release(Long id, int qty);
}