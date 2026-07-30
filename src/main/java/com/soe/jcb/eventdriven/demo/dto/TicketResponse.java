package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Ticket;
import com.soe.jcb.eventdriven.demo.entity.TicketStatus;

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
    public static TicketResponse from(Ticket ticket) {
        var orderItem = ticket.getOrderItem();
        var ticketType = orderItem.getTicketType();
        var event = ticketType.getEvent();
        var venue = event.getVenue();

        return new TicketResponse(
                ticket.getId(),
                ticket.getTicketCode(),
                ticket.getStatus(),
                ticket.getIssuedAt(),
                ticket.getValidatedAt(),
                event.getName(),
                event.getEventDate(),
                venue.getName(),
                ticketType.getName(),
                orderItem.getSeat() != null ? orderItem.getSeat().getRow() : null,
                orderItem.getSeat() != null ? orderItem.getSeat().getNumber() : null
        );
    }
}
