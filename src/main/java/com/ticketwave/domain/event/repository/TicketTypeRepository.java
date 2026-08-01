package com.ticketwave.domain.event.repository;

import com.ticketwave.domain.event.model.TicketType;
import java.util.List;
import java.util.Optional;

public interface TicketTypeRepository {
    Optional<TicketType> findById(Long id);
    List<TicketType> findByEventId(Long eventId);
    int incrementSoldQuantity(Long id, int qty);
    int decrementSoldQuantity(Long id, int qty);
}
