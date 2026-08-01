package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaTicketRepository extends JpaRepository<JpaTicket, Long> {

    Optional<JpaTicket> findByTicketCode(String ticketCode);

    List<JpaTicket> findByOrderId(Long orderId);

    @Query("SELECT t FROM JpaTicket t WHERE t.orderId IN (SELECT o.id FROM JpaOrder o WHERE o.userId = :userId)")
    List<JpaTicket> findByUserId(@Param("userId") Long userId);
}
