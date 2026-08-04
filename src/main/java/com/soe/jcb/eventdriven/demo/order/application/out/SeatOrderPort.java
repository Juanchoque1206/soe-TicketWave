package com.soe.jcb.eventdriven.demo.order.application.out;

import java.util.Optional;

/**
 * Anti-corruption port exposing just enough of the venue context for seat
 * assignment during order creation.
 */
public interface SeatOrderPort {

    Optional<SeatSnapshot> findById(Long id);

    boolean existsById(Long id);

    record SeatSnapshot(Long id, String row, int number) {
    }
}