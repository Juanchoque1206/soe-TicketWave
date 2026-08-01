package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.event.model.TicketType;
import com.ticketwave.domain.event.repository.TicketTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TicketTypePersistenceAdapter implements TicketTypeRepository {

    private final JpaTicketTypeRepository jpaTicketTypeRepository;
    private final EventPersistenceMapper mapper;

    public TicketTypePersistenceAdapter(JpaTicketTypeRepository jpaTicketTypeRepository,
                                         EventPersistenceMapper mapper) {
        this.jpaTicketTypeRepository = jpaTicketTypeRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<TicketType> findById(Long id) {
        return jpaTicketTypeRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<TicketType> findByEventId(Long eventId) {
        return jpaTicketTypeRepository.findByEventId(eventId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public int incrementSoldQuantity(Long id, int qty) {
        return jpaTicketTypeRepository.incrementSoldQuantity(id, qty);
    }

    @Override
    public int decrementSoldQuantity(Long id, int qty) {
        return jpaTicketTypeRepository.decrementSoldQuantity(id, qty);
    }
}
