package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.ticket.model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface JpaOrderRepository extends JpaRepository<JpaOrder, Long> {

    Optional<JpaOrder> findByOrderNumber(String orderNumber);

    List<JpaOrder> findByUserIdOrderByCreatedAtDesc(Long userId);

    long countByUserIdAndStatusAndCreatedAtAfter(Long userId, OrderStatus status, LocalDateTime after);
}
