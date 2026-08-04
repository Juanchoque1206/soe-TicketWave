package com.soe.jcb.eventdriven.demo.order.domain;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Domain port for persisting {@link Order}.
 */
public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countPendingSince(Long userId, LocalDateTime after);
}