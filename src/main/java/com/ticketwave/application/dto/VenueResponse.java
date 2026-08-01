package com.ticketwave.application.dto;

import java.io.Serializable;
import java.util.List;

public record VenueResponse(
        Long id,
        String name,
        String city,
        String address,
        int capacity,
        boolean hasAssignedSeating,
        List<SectionResponse> sections
) implements Serializable {}
