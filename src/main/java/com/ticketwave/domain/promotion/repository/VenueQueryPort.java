package com.ticketwave.domain.promotion.repository;

import com.ticketwave.application.dto.VenueResponse;

public interface VenueQueryPort {
    VenueResponse findVenueById(Long venueId);
}
