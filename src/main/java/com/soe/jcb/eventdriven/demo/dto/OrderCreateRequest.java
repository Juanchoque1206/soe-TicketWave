package com.soe.jcb.eventdriven.demo.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(
        @NotNull Long eventId,
        @NotEmpty List<OrderItemRequest> items,
        String promotionCode
) {
}
