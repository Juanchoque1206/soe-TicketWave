package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.VenueResponse;
import com.ticketwave.application.mapper.VenueMapper;
import com.ticketwave.application.usecase.FindVenueUseCase;
import com.ticketwave.domain.venue.repository.VenueRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindVenueUseCaseImpl implements FindVenueUseCase {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public FindVenueUseCaseImpl(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "venues", key = "#id")
    public VenueResponse findById(Long id) {
        return venueRepository.findById(id)
                .map(venueMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", id));
    }
}
