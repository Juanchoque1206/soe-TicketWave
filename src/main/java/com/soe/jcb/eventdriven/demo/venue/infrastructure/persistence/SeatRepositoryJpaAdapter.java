package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import org.springframework.stereotype.Component;

import java.util.Optional;

import com.soe.jcb.eventdriven.demo.venue.domain.Seat;
import com.soe.jcb.eventdriven.demo.venue.domain.SeatRepository;

@Component
public class SeatRepositoryJpaAdapter implements SeatRepository {

    private final SeatJpaRepository jpaRepository;

    public SeatRepositoryJpaAdapter(SeatJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Seat> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }

    private Seat toDomain(SeatJpaEntity entity) {
        return new Seat(entity.getId(), entity.getRow(), entity.getNumber());
    }
}