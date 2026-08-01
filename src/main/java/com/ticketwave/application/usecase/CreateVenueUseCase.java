package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.VenueRequest;
import com.ticketwave.application.dto.VenueResponse;

public interface CreateVenueUseCase {
    VenueResponse create(VenueRequest request);
}
