package com.soe.jcb.eventdriven.demo.event.dto;

import com.soe.jcb.eventdriven.demo.event.entity.EventCategory;
import com.soe.jcb.eventdriven.demo.event.entity.EventStatus;

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
) {
}
