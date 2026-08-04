package com.soe.jcb.eventdriven.demo.ticket.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketJpaRepository extends JpaRepository<TicketJpaEntity, Long> {

    Optional<TicketJpaEntity> findByTicketCode(String ticketCode);

    @Query("SELECT t FROM TicketJpaEntity t " +
            "WHERE t.orderItemId IN (SELECT oi.id FROM OrderItemJpaEntity oi " +
            "  WHERE oi.orderId IN (SELECT o.id FROM OrderJpaEntity o WHERE o.userId = :userId))")
    List<TicketJpaEntity> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM TicketJpaEntity t " +
            "WHERE t.orderItemId IN (SELECT oi.id FROM OrderItemJpaEntity oi WHERE oi.orderId = :orderId)")
    List<TicketJpaEntity> findByOrderId(@Param("orderId") Long orderId);
}