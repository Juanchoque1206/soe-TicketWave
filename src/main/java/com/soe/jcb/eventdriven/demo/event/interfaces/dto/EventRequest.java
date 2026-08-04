package com.soe.jcb.eventdriven.demo.event.interfaces.dto;

import com.soe.jcb.eventdriven.demo.event.application.in.EventUseCase;
import com.soe.jcb.eventdriven.demo.event.domain.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventRequest(
        String name,
        String description,
        Event.Category category,
        String artist,
        LocalDateTime eventDate,
        LocalDateTime salesStartDate,
        LocalDateTime salesEndDate,
        Long venueId,
        List<TicketTypeRequest> ticketTypes
) {
    public EventUseCase.CreateEventCommand toCreateCommand() {
        List<EventUseCase.TicketTypeCommand> commands = ticketTypes == null ? List.of()
                : ticketTypes.stream().map(TicketTypeRequest::toCommand).toList();
        return new EventUseCase.CreateEventCommand(name, description, category, artist,
                eventDate, salesStartDate, salesEndDate, venueId, commands);
    }

    public EventUseCase.UpdateEventCommand toUpdateCommand(Event.Status status) {
        return new EventUseCase.UpdateEventCommand(name, description, category, artist,
                eventDate, salesStartDate, salesEndDate, status);
    }

    public record TicketTypeRequest(
            String name,
            BigDecimal price,
            int totalQuantity,
            Long sectionId
    ) {
        EventUseCase.TicketTypeCommand toCommand() {
            return new EventUseCase.TicketTypeCommand(name, price, totalQuantity, sectionId);
        }
    }
}