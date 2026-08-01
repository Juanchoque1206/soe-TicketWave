package com.ticketwave.domain.payment.repository;

import com.ticketwave.domain.payment.model.Payment;

import java.util.Optional;

public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(Long orderId);
}
