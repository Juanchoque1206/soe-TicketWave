package com.soe.jcb.eventdriven.demo.event.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.event.domain.TicketType;
import com.soe.jcb.eventdriven.demo.event.domain.TicketTypeRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class TicketTypeRepositoryJpaAdapter implements TicketTypeRepository {

    private final TicketTypeJpaRepository jpaRepository;

    public TicketTypeRepositoryJpaAdapter(TicketTypeJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<TicketType> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<TicketType> findByEventId(Long eventId) {
        return jpaRepository.findByEventId(eventId).stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public boolean reserve(Long id, int qty) {
        return jpaRepository.reserve(id, qty) > 0;
    }

    @Override
    @Transactional
    public boolean release(Long id, int qty) {
        return jpaRepository.release(id, qty) > 0;
    }

    private TicketType toDomain(TicketTypeJpaEntity entity) {
        return new TicketType(entity.getId(), entity.getName(), entity.getPrice(),
                entity.getTotalQuantity(), entity.getSoldQuantity(), entity.getSectionId());
    }
}