package com.soe.jcb.eventdriven.demo.venue.domain;

import java.util.Optional;

public interface SeatRepository {

    Optional<Seat> findById(Long id);

    boolean existsById(Long id);
}