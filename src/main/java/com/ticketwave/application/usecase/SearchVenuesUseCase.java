package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.VenueResponse;

import java.util.List;

public interface SearchVenuesUseCase {
    List<VenueResponse> findByCity(String city);
    List<VenueResponse> findAll();
}
