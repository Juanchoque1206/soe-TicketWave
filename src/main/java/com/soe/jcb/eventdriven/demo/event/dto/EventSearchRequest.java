package com.soe.jcb.eventdriven.demo.event.dto;

import java.time.LocalDateTime;

public record EventSearchRequest(
        String city,
        String artist,
        Long venueId,
        LocalDateTime from,
        LocalDateTime to
) {
}
