package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Event;
import com.soe.jcb.eventdriven.demo.entity.EventCategory;
import com.soe.jcb.eventdriven.demo.entity.EventStatus;

import java.time.LocalDateTime;
import java.util.List;

public record EventResponse(
        Long id,
        String name,
        String description,
        EventCategory category,
        EventStatus status,
        String artist,
        LocalDateTime eventDate,
        LocalDateTime salesStartDate,
        LocalDateTime salesEndDate,
        Long venueId,
        String venueName,
        String venueCity,
        List<TicketTypeResponse> ticketTypes
) {
    public static EventResponse from(Event event) {
        List<TicketTypeResponse> ticketTypes = event.getTicketTypes().stream()
                .map(TicketTypeResponse::from)
                .toList();
        return new EventResponse(
                event.getId(),
                event.getName(),
                event.getDescription(),
                event.getCategory(),
                event.getStatus(),
                event.getArtist(),
                event.getEventDate(),
                event.getSalesStartDate(),
                event.getSalesEndDate(),
                event.getVenue().getId(),
                event.getVenue().getName(),
                event.getVenue().getCity(),
                ticketTypes
        );
    }
}
