package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.VenueResponse;

public interface FindVenueUseCase {
    VenueResponse findById(Long id);
}
