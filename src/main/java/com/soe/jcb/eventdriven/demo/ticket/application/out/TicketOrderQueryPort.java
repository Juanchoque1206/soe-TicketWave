package com.soe.jcb.eventdriven.demo.ticket.application.out;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Anti-corruption port exposing order/event/venue metadata needed to build a
 * ticket response, without importing order/event/venue domain types.
 */
public interface TicketOrderQueryPort {

    List<OrderItemSnapshot> findOrderItemsByOrderId(Long orderId);

    record OrderItemSnapshot(Long id, String eventName, LocalDateTime eventDate,
                             String venueName, String ticketTypeName, String seatRow, Integer seatNumber) {
    }
}