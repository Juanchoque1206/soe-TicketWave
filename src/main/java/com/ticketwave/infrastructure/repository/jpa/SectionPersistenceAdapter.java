package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.venue.model.Section;
import com.ticketwave.domain.venue.repository.SectionRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SectionPersistenceAdapter implements SectionRepository {

    private final JpaSectionRepository jpaSectionRepository;
    private final VenuePersistenceMapper mapper;

    public SectionPersistenceAdapter(JpaSectionRepository jpaSectionRepository, VenuePersistenceMapper mapper) {
        this.jpaSectionRepository = jpaSectionRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Section> findById(Long id) {
        return jpaSectionRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Section> findByVenueId(Long venueId) {
        return jpaSectionRepository.findByVenueId(venueId).stream().map(mapper::toDomain).toList();
    }
}
