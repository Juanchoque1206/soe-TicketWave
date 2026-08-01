package com.ticketwave.application.dto;

import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record OrderItemRequest(
        @NotNull Long ticketTypeId,
        Long seatId
) implements Serializable {
}
