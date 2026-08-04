package com.soe.jcb.eventdriven.demo.ticket.domain;

import java.util.List;
import java.util.Optional;

/**
 * Domain port for persisting {@link Ticket}.
 */
public interface TicketRepository {

    List<Ticket> saveAll(List<Ticket> tickets);

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findByUserId(Long userId);

    List<Ticket> findByOrderId(Long orderId);
}