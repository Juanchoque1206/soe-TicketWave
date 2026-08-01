package com.ticketwave.domain.ticket.repository;

import com.ticketwave.domain.ticket.model.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketRepository {

    Ticket save(Ticket ticket);

    List<Ticket> saveAll(List<Ticket> tickets);

    Optional<Ticket> findByTicketCode(String ticketCode);

    List<Ticket> findByOrderId(Long orderId);

    List<Ticket> findByUserId(Long userId);
}
