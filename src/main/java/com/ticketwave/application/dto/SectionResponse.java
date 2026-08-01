package com.ticketwave.application.dto;

import java.io.Serializable;

public record SectionResponse(
        Long id,
        String name,
        int capacity,
        boolean generalAdmission,
        int seatCount
) implements Serializable {}
