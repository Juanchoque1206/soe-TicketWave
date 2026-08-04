package com.soe.jcb.eventdriven.demo.ticket.interfaces.dto;

import com.soe.jcb.eventdriven.demo.ticket.application.in.TicketUseCase;
import com.soe.jcb.eventdriven.demo.ticket.domain.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponse(
        Long id,
        String ticketCode,
        TicketStatus status,
        LocalDateTime issuedAt,
        LocalDateTime validatedAt,
        String eventName,
        LocalDateTime eventDate,
        String venueName,
        String ticketTypeName,
        String seatRow,
        Integer seatNumber
) {
    public static TicketResponse from(TicketUseCase.TicketResult result) {
        return new TicketResponse(result.id(), result.ticketCode(), result.status(), result.issuedAt(),
                result.validatedAt(), result.eventName(), result.eventDate(), result.venueName(),
                result.ticketTypeName(), result.seatRow(), result.seatNumber());
    }
}