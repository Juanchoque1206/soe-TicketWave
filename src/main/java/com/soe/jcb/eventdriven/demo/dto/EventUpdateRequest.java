package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.EventCategory;
import com.soe.jcb.eventdriven.demo.entity.EventStatus;

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
