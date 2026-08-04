package com.soe.jcb.eventdriven.demo.order.application.out;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Anti-corruption port exposing just enough of the event context for order
 * creation, without importing event domain types.
 */
public interface EventOrderPort {

    Optional<EventSnapshot> findById(Long eventId);

    record EventSnapshot(Long id, String status, LocalDateTime salesStartDate, LocalDateTime salesEndDate) {
    }
}