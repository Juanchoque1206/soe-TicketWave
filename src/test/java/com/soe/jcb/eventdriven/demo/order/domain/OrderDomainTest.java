package com.soe.jcb.eventdriven.demo.order.domain;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderDomainTest {

    private Order pendingOrder() {
        return new Order(1L, "ORD-1", 42L, OrderStatus.PENDING, BigDecimal.TEN,
                null, BigDecimal.ZERO, LocalDateTime.now());
    }

    @Test
    void confirmTransitionsFromPending() {
        Order order = pendingOrder();
        order.confirm();
        assertEquals(OrderStatus.CONFIRMED, order.getStatus());
    }

    @Test
    void cancelTransitionsFromPendingOrConfirmed() {
        Order pending = pendingOrder();
        pending.cancel();
        assertEquals(OrderStatus.CANCELLED, pending.getStatus());

        Order confirmed = new Order(1L, "ORD-1", 42L, OrderStatus.CONFIRMED, BigDecimal.TEN,
                null, BigDecimal.ZERO, LocalDateTime.now());
        confirmed.cancel();
        assertEquals(OrderStatus.CANCELLED, confirmed.getStatus());
    }

    @Test
    void confirmFromCancelledThrows() {
        Order order = new Order(1L, "ORD-1", 42L, OrderStatus.CANCELLED, BigDecimal.TEN,
                null, BigDecimal.ZERO, LocalDateTime.now());
        assertThrows(IllegalStateException.class, order::confirm);
    }

    @Test
    void cancelFromRefundedThrows() {
        Order order = new Order(1L, "ORD-1", 42L, OrderStatus.REFUNDED, BigDecimal.TEN,
                null, BigDecimal.ZERO, LocalDateTime.now());
        assertThrows(IllegalStateException.class, order::cancel);
    }

    @Test
    void itemsAccumulateUnitPrices() {
        Order order = pendingOrder();
        order.addItem(new OrderItem(null, 100L, "GA", new BigDecimal("49.99"), null, null, null));
        order.addItem(new OrderItem(null, 101L, "VIP", new BigDecimal("149.99"), null, null, null));
        assertEquals(2, order.getItems().size());
    }
}