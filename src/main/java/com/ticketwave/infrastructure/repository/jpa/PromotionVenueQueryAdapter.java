package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.promotion.repository.VenueQueryPort;
import com.ticketwave.application.dto.VenueResponse;
import com.ticketwave.application.usecase.FindVenueUseCase;
import org.springframework.stereotype.Component;

@Component("promotionVenueQueryAdapter")
public class PromotionVenueQueryAdapter implements VenueQueryPort {

    private final FindVenueUseCase findVenueUseCase;

    public PromotionVenueQueryAdapter(FindVenueUseCase findVenueUseCase) {
        this.findVenueUseCase = findVenueUseCase;
    }

    @Override
    public VenueResponse findVenueById(Long venueId) {
        return findVenueUseCase.findById(venueId);
    }
}
