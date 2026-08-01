package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.venue.model.Venue;
import com.ticketwave.domain.venue.repository.VenueRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VenuePersistenceAdapter implements VenueRepository {

    private final JpaVenueRepository jpaVenueRepository;
    private final VenuePersistenceMapper mapper;

    public VenuePersistenceAdapter(JpaVenueRepository jpaVenueRepository, VenuePersistenceMapper mapper) {
        this.jpaVenueRepository = jpaVenueRepository;
        this.mapper = mapper;
    }

    @Override
    public Venue save(Venue venue) {
        JpaVenue jpaVenue = mapper.toJpa(venue);
        jpaVenue = jpaVenueRepository.save(jpaVenue);
        return mapper.toDomain(jpaVenue);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return jpaVenueRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Venue> findByCity(String city) {
        return jpaVenueRepository.findByCity(city).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Venue> findAll() {
        return jpaVenueRepository.findAll().stream().map(mapper::toDomain).toList();
    }
}
