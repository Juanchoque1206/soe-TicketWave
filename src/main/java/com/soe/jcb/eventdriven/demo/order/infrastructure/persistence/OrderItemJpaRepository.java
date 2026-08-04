package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderItemJpaRepository extends JpaRepository<OrderItemJpaEntity, Long> {

    @Query("SELECT COUNT(oi) > 0 FROM OrderItemJpaEntity oi " +
            "WHERE oi.seatId = :seatId " +
            "AND oi.ticketTypeId IN (SELECT tt.id FROM TicketTypeJpaEntity tt WHERE tt.eventId = :eventId) " +
            "AND oi.orderId IN (SELECT o.id FROM OrderJpaEntity o " +
            "  WHERE o.status IN (com.soe.jcb.eventdriven.demo.order.domain.OrderStatus.PENDING, " +
            "                     com.soe.jcb.eventdriven.demo.order.domain.OrderStatus.CONFIRMED))")
    boolean isSeatReserved(@Param("eventId") Long eventId, @Param("seatId") Long seatId);
}