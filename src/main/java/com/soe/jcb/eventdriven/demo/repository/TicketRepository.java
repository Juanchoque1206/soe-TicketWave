package com.soe.jcb.eventdriven.demo.repository;

import com.soe.jcb.eventdriven.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Optional<Ticket> findByTicketCode(String ticketCode);

    @Query("SELECT t FROM Ticket t WHERE t.orderItem.order.user.id = :userId")
    List<Ticket> findByUserId(@Param("userId") Long userId);

    @Query("SELECT t FROM Ticket t WHERE t.orderItem.order.id = :orderId")
    List<Ticket> findByOrderId(@Param("orderId") Long orderId);
}
