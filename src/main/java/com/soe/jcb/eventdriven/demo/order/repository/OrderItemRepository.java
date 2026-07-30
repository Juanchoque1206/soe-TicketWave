package com.soe.jcb.eventdriven.demo.order.repository;

import com.soe.jcb.eventdriven.demo.order.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    List<OrderItem> findByOrderId(Long orderId);

    @Query("SELECT oi FROM OrderItem oi WHERE oi.ticketType.event.id = :eventId " +
            "AND oi.seat.id = :seatId " +
            "AND oi.order.status IN (com.soe.jcb.eventdriven.demo.entity.OrderStatus.PENDING, " +
            "com.soe.jcb.eventdriven.demo.entity.OrderStatus.CONFIRMED)")
    Optional<OrderItem> findActiveBySeatAndEvent(@Param("eventId") Long eventId,
                                                  @Param("seatId") Long seatId);
}
