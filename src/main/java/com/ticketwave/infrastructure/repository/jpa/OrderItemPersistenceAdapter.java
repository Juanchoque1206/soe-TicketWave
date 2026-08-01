package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.ticket.model.OrderItem;
import com.ticketwave.domain.ticket.repository.OrderItemRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderItemPersistenceAdapter implements OrderItemRepository {

    private final JpaOrderItemRepository jpaOrderItemRepository;
    private final TicketPersistenceMapper mapper;

    public OrderItemPersistenceAdapter(JpaOrderItemRepository jpaOrderItemRepository,
                                       TicketPersistenceMapper mapper) {
        this.jpaOrderItemRepository = jpaOrderItemRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<OrderItem> findActiveBySeatAndEvent(Long eventId, Long seatId) {
        return jpaOrderItemRepository.findActiveBySeatAndEvent(eventId, seatId)
                .map(mapper::toDomain);
    }
}
