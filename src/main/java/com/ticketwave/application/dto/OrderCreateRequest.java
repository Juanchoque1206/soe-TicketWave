package com.ticketwave.application.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

public record OrderCreateRequest(
        @NotNull Long eventId,
        @NotEmpty List<OrderItemRequest> items,
        String promotionCode
) implements Serializable {
}
