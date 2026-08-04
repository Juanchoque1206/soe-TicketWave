package com.soe.jcb.eventdriven.demo.payment.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.payment.domain.Payment;
import com.soe.jcb.eventdriven.demo.payment.domain.PaymentRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class PaymentRepositoryJpaAdapter implements PaymentRepository {

    private final PaymentJpaRepository jpaRepository;

    public PaymentRepositoryJpaAdapter(PaymentJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Payment save(Payment payment) {
        PaymentJpaEntity entity = toJpa(payment);
        PaymentJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).map(this::toDomain);
    }

    private PaymentJpaEntity toJpa(Payment payment) {
        PaymentJpaEntity entity = new PaymentJpaEntity();
        entity.setId(payment.getId());
        entity.setOrderId(payment.getOrderId());
        entity.setExternalTransactionId(payment.getExternalTransactionId());
        entity.setAmount(payment.getAmount());
        entity.setMethod(payment.getMethod());
        entity.setStatus(payment.getStatus());
        entity.setPaidAt(payment.getPaidAt());
        entity.setRefundedAt(payment.getRefundedAt());
        return entity;
    }

    private Payment toDomain(PaymentJpaEntity entity) {
        return new Payment(entity.getId(), entity.getOrderId(), entity.getExternalTransactionId(),
                entity.getAmount(), entity.getMethod(), entity.getStatus(), entity.getPaidAt(),
                entity.getRefundedAt());
    }
}