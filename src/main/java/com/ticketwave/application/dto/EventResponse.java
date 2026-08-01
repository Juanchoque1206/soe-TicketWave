package com.ticketwave.application.dto;

import com.ticketwave.domain.event.model.EventCategory;
import com.ticketwave.domain.event.model.EventStatus;

import java.io.Serializable;
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
) implements Serializable {}
