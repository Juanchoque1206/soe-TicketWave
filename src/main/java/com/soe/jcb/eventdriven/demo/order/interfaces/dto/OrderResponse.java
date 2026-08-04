package com.soe.jcb.eventdriven.demo.order.interfaces.dto;

import com.soe.jcb.eventdriven.demo.order.application.in.OrderUseCase;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        String orderNumber,
        String status,
        BigDecimal totalAmount,
        String promotionCode,
        BigDecimal discountAmount,
        LocalDateTime createdAt,
        List<ItemResponse> items
) {
    public static OrderResponse from(OrderUseCase.OrderResult result) {
        List<ItemResponse> items = result.items() == null ? List.of()
                : result.items().stream().map(ItemResponse::from).toList();
        return new OrderResponse(result.id(), result.orderNumber(), result.status(), result.totalAmount(),
                result.promotionCode(), result.discountAmount(), result.createdAt(), items);
    }

    public record ItemResponse(
            Long id,
            String ticketTypeName,
            BigDecimal unitPrice,
            Long seatId,
            String seatRow,
            Integer seatNumber
    ) {
        public static ItemResponse from(OrderUseCase.OrderItemResult result) {
            return new ItemResponse(result.id(), result.ticketTypeName(), result.unitPrice(),
                    result.seatId(), result.seatRow(), result.seatNumber());
        }
    }
}