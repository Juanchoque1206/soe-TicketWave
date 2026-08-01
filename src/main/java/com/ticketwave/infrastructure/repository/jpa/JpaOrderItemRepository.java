package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface JpaOrderItemRepository extends JpaRepository<JpaOrderItem, Long> {

    @Query("SELECT oi FROM JpaOrderItem oi WHERE oi.seatId = :seatId AND oi.order.eventId = :eventId " +
            "AND oi.order.status IN (com.ticketwave.domain.ticket.model.OrderStatus.PENDING, " +
            "com.ticketwave.domain.ticket.model.OrderStatus.CONFIRMED)")
    Optional<JpaOrderItem> findActiveBySeatAndEvent(@Param("eventId") Long eventId,
                                                    @Param("seatId") Long seatId);
}
