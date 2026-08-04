package com.soe.jcb.eventdriven.demo.event.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.event.application.out.VenueExistsChecker;
import com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence.VenueJpaRepository;
import org.springframework.stereotype.Component;

@Component("eventVenueExistsChecker")
public class EventVenueExistsCheckerAdapter implements VenueExistsChecker {

    private final VenueJpaRepository venueJpaRepository;

    public EventVenueExistsCheckerAdapter(VenueJpaRepository venueJpaRepository) {
        this.venueJpaRepository = venueJpaRepository;
    }

    @Override
    public boolean existsById(Long venueId) {
        return venueJpaRepository.existsById(venueId);
    }
}