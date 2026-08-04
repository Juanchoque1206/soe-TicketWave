package com.soe.jcb.eventdriven.demo.order.application.out;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Anti-corruption port exposing just enough of the event ticket-type inventory
 * for order creation and cancellation.
 */
public interface TicketTypeOrderPort {

    Optional<TicketTypeSnapshot> findById(Long id);

    boolean belongsToEvent(Long ticketTypeId, Long eventId);

    boolean reserve(Long id, int qty);

    boolean release(Long id, int qty);

    record TicketTypeSnapshot(Long id, Long eventId, String name, BigDecimal price) {
    }
}