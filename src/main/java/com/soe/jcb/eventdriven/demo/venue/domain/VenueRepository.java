package com.soe.jcb.eventdriven.demo.venue.domain;

import java.util.List;
import java.util.Optional;

public interface VenueRepository {

    Venue save(Venue venue);

    Optional<Venue> findById(Long id);

    List<Venue> findByCity(String city);

    List<Venue> findAll();
}