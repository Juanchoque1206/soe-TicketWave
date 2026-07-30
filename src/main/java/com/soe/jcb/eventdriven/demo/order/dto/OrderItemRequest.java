package com.soe.jcb.eventdriven.demo.order.dto;

import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull Long ticketTypeId,
        Long seatId
) {
}
