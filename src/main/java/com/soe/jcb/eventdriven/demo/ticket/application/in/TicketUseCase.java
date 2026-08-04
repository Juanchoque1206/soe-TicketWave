package com.soe.jcb.eventdriven.demo.ticket.application.in;

import com.soe.jcb.eventdriven.demo.ticket.domain.Ticket;
import com.soe.jcb.eventdriven.demo.ticket.domain.TicketStatus;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Use-case boundary for ticket management (pure, no Spring).
 */
public interface TicketUseCase {

    List<TicketResult> issueTickets(Long orderId);

    List<TicketResult> findByUser(Long userId);

    TicketResult findByTicketCode(String ticketCode);

    TicketResult validateTicket(String ticketCode);

    record TicketResult(Long id, String ticketCode, TicketStatus status, LocalDateTime issuedAt,
                        LocalDateTime validatedAt, String eventName, LocalDateTime eventDate,
                        String venueName, String ticketTypeName, String seatRow, Integer seatNumber) {
    }

    record TicketCommand(Long orderId) {
    }
}