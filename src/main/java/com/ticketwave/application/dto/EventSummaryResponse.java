package com.ticketwave.application.dto;

import com.ticketwave.domain.event.model.EventCategory;
import com.ticketwave.domain.event.model.EventStatus;

import java.io.Serializable;
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
) implements Serializable {}
