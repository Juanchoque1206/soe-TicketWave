package com.ticketwave.domain.ticket.repository;

import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(Long id);

    Optional<Order> findByOrderNumber(String orderNumber);

    List<Order> findByUserId(Long userId);

    long countByUserIdAndStatusAndCreatedAtAfter(Long userId, OrderStatus status, LocalDateTime after);
}
