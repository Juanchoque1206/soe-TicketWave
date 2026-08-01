package com.ticketwave.application.mapper;

import com.ticketwave.application.dto.TicketResponse;
import com.ticketwave.domain.ticket.model.Ticket;
import org.springframework.stereotype.Component;

@Component
public class TicketMapper {

    public TicketResponse toResponse(Ticket ticket) {
        return new TicketResponse(
                ticket.getId(),
                ticket.getTicketCode(),
                ticket.getOrderId(),
                ticket.getStatus(),
                ticket.getIssuedAt(),
                ticket.getValidatedAt(),
                ticket.getEventName(),
                ticket.getEventDate(),
                ticket.getVenueName(),
                ticket.getTicketTypeName(),
                ticket.getSeatRow(),
                ticket.getSeatNumber());
    }
}
