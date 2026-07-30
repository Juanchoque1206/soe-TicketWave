package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Event;
import com.soe.jcb.eventdriven.demo.entity.EventCategory;
import com.soe.jcb.eventdriven.demo.entity.EventStatus;

import java.time.LocalDateTime;

public record EventSummaryResponse(
        Long id,
        String name,
        EventCategory category,
        EventStatus status,
        String artist,
        LocalDateTime eventDate,
        String venueName,
        String venueCity
) {
    public static EventSummaryResponse from(Event event) {
        return new EventSummaryResponse(
                event.getId(),
                event.getName(),
                event.getCategory(),
                event.getStatus(),
                event.getArtist(),
                event.getEventDate(),
                event.getVenue().getName(),
                event.getVenue().getCity()
        );
    }
}
