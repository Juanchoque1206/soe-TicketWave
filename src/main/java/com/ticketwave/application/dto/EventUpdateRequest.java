package com.ticketwave.application.dto;

import com.ticketwave.domain.event.model.EventCategory;
import com.ticketwave.domain.event.model.EventStatus;

import java.time.LocalDateTime;

public record EventUpdateRequest(
        String name,
        String description,
        EventCategory category,
        String artist,
        LocalDateTime eventDate,
        LocalDateTime salesStartDate,
        LocalDateTime salesEndDate,
        EventStatus status
) {}
