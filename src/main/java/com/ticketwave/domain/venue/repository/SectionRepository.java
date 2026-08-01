package com.ticketwave.domain.venue.repository;

import com.ticketwave.domain.venue.model.Section;

import java.util.List;
import java.util.Optional;

public interface SectionRepository {
    Optional<Section> findById(Long id);
    List<Section> findByVenueId(Long venueId);
}
