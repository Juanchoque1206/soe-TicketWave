package com.soe.jcb.eventdriven.demo.payment.domain;

import java.util.Optional;

/**
 * Domain port for persisting {@link Payment}.
 */
public interface PaymentRepository {

    Payment save(Payment payment);

    Optional<Payment> findByOrderId(Long orderId);
}