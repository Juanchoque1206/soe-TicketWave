package com.ticketwave.application.dto;

import com.ticketwave.domain.ticket.model.TicketStatus;

import java.io.Serializable;
import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String ticketCode,
        Long orderId,
        TicketStatus status,
        LocalDateTime issuedAt,
        LocalDateTime validatedAt,
        String eventName,
        LocalDateTime eventDate,
        String venueName,
        String ticketTypeName,
        String seatRow,
        Integer seatNumber
) implements Serializable {
}
