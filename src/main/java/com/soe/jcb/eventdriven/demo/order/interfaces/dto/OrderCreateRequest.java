package com.soe.jcb.eventdriven.demo.order.interfaces.dto;

import com.soe.jcb.eventdriven.demo.order.application.in.OrderUseCase;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record OrderCreateRequest(
        @NotNull Long eventId,
        @NotEmpty List<ItemRequest> items,
        String promotionCode
) {
    public OrderUseCase.CreateOrderCommand toCommand(Long userId) {
        List<OrderUseCase.CreateOrderItemCommand> itemCommands = items.stream()
                .map(i -> new OrderUseCase.CreateOrderItemCommand(i.ticketTypeId(), i.seatId()))
                .toList();
        return new OrderUseCase.CreateOrderCommand(userId, eventId, promotionCode, itemCommands);
    }

    public record ItemRequest(
            @NotNull Long ticketTypeId,
            Long seatId
    ) {
    }
}