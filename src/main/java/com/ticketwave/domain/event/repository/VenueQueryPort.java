package com.ticketwave.domain.event.repository;

import com.ticketwave.application.dto.VenueResponse;

public interface VenueQueryPort {
    VenueResponse findVenueById(Long venueId);
}
