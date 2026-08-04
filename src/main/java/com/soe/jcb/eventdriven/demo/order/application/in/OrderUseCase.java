package com.soe.jcb.eventdriven.demo.order.application.in;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use-case boundary for order management (pure, no Spring).
 */
public interface OrderUseCase {

    OrderResult create(CreateOrderCommand command);

    OrderResult confirm(Long orderId);

    OrderResult cancel(String orderNumber, Long userId);

    OrderResult findByOrderNumber(String orderNumber, Long userId);

    OrderResult findByOrderNumber(String orderNumber);

    List<OrderResult> findByUser(Long userId);

    record CreateOrderCommand(Long userId, Long eventId, String promotionCode,
                              List<CreateOrderItemCommand> items) {
    }

    record CreateOrderItemCommand(Long ticketTypeId, Long seatId) {
    }

    record OrderItemResult(Long id, String ticketTypeName, BigDecimal unitPrice,
                           Long seatId, String seatRow, Integer seatNumber) {
    }

    record OrderResult(Long id, String orderNumber, String status, BigDecimal totalAmount,
                       String promotionCode, BigDecimal discountAmount, LocalDateTime createdAt,
                       List<OrderItemResult> items) {
    }
}