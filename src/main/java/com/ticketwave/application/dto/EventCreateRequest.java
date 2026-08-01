package com.ticketwave.application.dto;

import com.ticketwave.domain.event.model.EventCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record EventCreateRequest(
        @NotBlank String name,
        String description,
        @NotNull EventCategory category,
        String artist,
        @NotNull LocalDateTime eventDate,
        LocalDateTime salesStartDate,
        LocalDateTime salesEndDate,
        @NotNull Long venueId,
        @NotEmpty List<TicketTypeRequest> ticketTypes
) {
    public record TicketTypeRequest(
            @NotBlank String name,
            @NotNull BigDecimal price,
            int totalQuantity,
            Long sectionId
    ) {}
}
