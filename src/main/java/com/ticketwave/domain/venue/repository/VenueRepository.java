package com.ticketwave.domain.venue.repository;

import com.ticketwave.domain.venue.model.Venue;

import java.util.List;
import java.util.Optional;

public interface VenueRepository {
    Venue save(Venue venue);
    Optional<Venue> findById(Long id);
    List<Venue> findByCity(String city);
    List<Venue> findAll();
}
