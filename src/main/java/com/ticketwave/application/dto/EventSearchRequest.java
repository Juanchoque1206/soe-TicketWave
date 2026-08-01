package com.ticketwave.application.dto;

import java.time.LocalDateTime;

public record EventSearchRequest(
        String city,
        String artist,
        Long venueId,
        LocalDateTime from,
        LocalDateTime to
) {}
