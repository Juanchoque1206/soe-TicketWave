package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPaymentRepository extends JpaRepository<JpaPayment, Long> {

    Optional<JpaPayment> findByOrderId(Long orderId);
}
