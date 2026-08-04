package com.soe.jcb.eventdriven.demo.promotion.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.promotion.application.out.VenueExistsChecker;
import com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence.VenueJpaRepository;
import org.springframework.stereotype.Component;

@Component("promotionVenueExistsChecker")
public class PromotionVenueExistsCheckerAdapter implements VenueExistsChecker {

    private final VenueJpaRepository venueJpaRepository;

    public PromotionVenueExistsCheckerAdapter(VenueJpaRepository venueJpaRepository) {
        this.venueJpaRepository = venueJpaRepository;
    }

    @Override
    public boolean existsById(Long venueId) {
        return venueJpaRepository.existsById(venueId);
    }
}