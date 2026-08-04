package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.order.application.out.SeatOrderPort;
import com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence.SeatJpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Adapter bridging the order context's {@link SeatOrderPort} to the venue
 * context's seat persistence.
 */
@Component
public class SeatOrderPortAdapter implements SeatOrderPort {

    private final SeatJpaRepository jpaRepository;

    public SeatOrderPortAdapter(SeatJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<SeatSnapshot> findById(Long id) {
        return jpaRepository.findById(id)
                .map(s -> new SeatSnapshot(s.getId(), s.getRow(), s.getNumber()));
    }

    @Override
    public boolean existsById(Long id) {
        return jpaRepository.existsById(id);
    }
}