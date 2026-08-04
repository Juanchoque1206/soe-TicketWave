package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.venue.domain.Seat;

/**
 * Maps between the pure domain {@link Seat} and {@link SeatJpaEntity}.
 */
public class SeatMapper {

    public SeatJpaEntity toJpa(Seat seat, SectionJpaEntity section) {
        SeatJpaEntity entity = new SeatJpaEntity();
        entity.setId(seat.getId());
        entity.setRow(seat.getRow());
        entity.setNumber(seat.getNumber());
        entity.setSection(section);
        return entity;
    }

    public Seat toDomain(SeatJpaEntity entity) {
        return new Seat(entity.getId(), entity.getRow(), entity.getNumber());
    }
}