package com.ticketwave.application.dto;

import com.ticketwave.domain.venue.model.SeatStatus;

import java.io.Serializable;

public record SeatResponse(
        Long id,
        String row,
        int number,
        SeatStatus status
) implements Serializable {}
