package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.VenueResponse;
import com.ticketwave.application.mapper.VenueMapper;
import com.ticketwave.application.usecase.SearchVenuesUseCase;
import com.ticketwave.domain.venue.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SearchVenuesUseCaseImpl implements SearchVenuesUseCase {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public SearchVenuesUseCaseImpl(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueResponse> findByCity(String city) {
        return venueRepository.findByCity(city).stream()
                .map(venueMapper::toResponseWithoutSections)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<VenueResponse> findAll() {
        return venueRepository.findAll().stream()
                .map(venueMapper::toResponseWithoutSections)
                .toList();
    }
}
