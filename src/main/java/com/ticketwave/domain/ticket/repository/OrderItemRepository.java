package com.ticketwave.domain.ticket.repository;

import com.ticketwave.domain.ticket.model.OrderItem;

import java.util.Optional;

public interface OrderItemRepository {

    Optional<OrderItem> findActiveBySeatAndEvent(Long eventId, Long seatId);
}
