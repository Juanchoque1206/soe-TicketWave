package com.ticketwave.application.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record VenueRequest(
        @NotBlank String name,
        @NotBlank String city,
        @NotBlank String address,
        @NotNull @Min(1) Integer capacity,
        boolean hasAssignedSeating,
        List<SectionRequest> sections
) {
    public record SectionRequest(
            @NotBlank String name,
            @Min(1) int capacity,
            boolean generalAdmission,
            List<SeatRequest> seats
    ) {
    }

    public record SeatRequest(
            @NotBlank String row,
            int number
    ) {
    }
}
