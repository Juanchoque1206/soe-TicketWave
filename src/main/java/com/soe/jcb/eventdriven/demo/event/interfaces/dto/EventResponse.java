package com.soe.jcb.eventdriven.demo.event.interfaces.dto;

import com.soe.jcb.eventdriven.demo.event.application.in.EventUseCase;
import com.soe.jcb.eventdriven.demo.event.domain.Event;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(
        Long id,
        String name,
        String description,
        Event.Category category,
        Event.Status status,
        String artist,
        LocalDateTime eventDate,
        LocalDateTime salesStartDate,
        LocalDateTime salesEndDate,
        Long venueId,
        List<TicketTypeResponse> ticketTypes
) {
    public static EventResponse from(EventUseCase.EventResult result) {
        List<TicketTypeResponse> ticketTypes = result.ticketTypes() == null ? List.of()
                : result.ticketTypes().stream().map(TicketTypeResponse::from).toList();
        return new EventResponse(result.id(), result.name(), result.description(), result.category(),
                result.status(), result.artist(), result.eventDate(), result.salesStartDate(),
                result.salesEndDate(), result.venueId(), ticketTypes);
    }

    public record TicketTypeResponse(
            Long id,
            String name,
            java.math.BigDecimal price,
            int totalQuantity,
            int soldQuantity,
            Long sectionId
    ) {
        public static TicketTypeResponse from(EventUseCase.TicketTypeResult result) {
            return new TicketTypeResponse(result.id(), result.name(), result.price(),
                    result.totalQuantity(), result.soldQuantity(), result.sectionId());
        }
    }
}