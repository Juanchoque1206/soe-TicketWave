package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.venue.model.Seat;
import com.ticketwave.domain.venue.repository.SeatRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class SeatPersistenceAdapter implements SeatRepository {

    private final JpaSeatRepository jpaSeatRepository;
    private final VenuePersistenceMapper mapper;

    public SeatPersistenceAdapter(JpaSeatRepository jpaSeatRepository, VenuePersistenceMapper mapper) {
        this.jpaSeatRepository = jpaSeatRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Seat> findById(Long id) {
        return jpaSeatRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Seat> findBySectionId(Long sectionId) {
        return jpaSeatRepository.findBySectionId(sectionId).stream().map(mapper::toDomain).toList();
    }
}
