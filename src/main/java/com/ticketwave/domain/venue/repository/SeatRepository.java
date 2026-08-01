package com.ticketwave.domain.venue.repository;

import com.ticketwave.domain.venue.model.Seat;

import java.util.List;
import java.util.Optional;

public interface SeatRepository {
    Optional<Seat> findById(Long id);
    List<Seat> findBySectionId(Long sectionId);
}
