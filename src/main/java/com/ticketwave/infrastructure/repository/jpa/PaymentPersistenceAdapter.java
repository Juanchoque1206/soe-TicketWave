package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.domain.payment.model.Payment;
import com.ticketwave.domain.payment.repository.PaymentRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class PaymentPersistenceAdapter implements PaymentRepository {

    private final JpaPaymentRepository jpaPaymentRepository;
    private final PaymentPersistenceMapper mapper;

    public PaymentPersistenceAdapter(JpaPaymentRepository jpaPaymentRepository,
                                     PaymentPersistenceMapper mapper) {
        this.jpaPaymentRepository = jpaPaymentRepository;
        this.mapper = mapper;
    }

    @Override
    public Payment save(Payment payment) {
        JpaPayment jpa;
        if (payment.getId() != null) {
            jpa = jpaPaymentRepository.findById(payment.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Payment", "id", payment.getId()));
            jpa.setOrderId(payment.getOrderId());
            jpa.setOrderNumber(payment.getOrderNumber());
            jpa.setUserId(payment.getUserId());
            jpa.setAmount(payment.getAmount());
            jpa.setMethod(payment.getMethod());
            jpa.setStatus(payment.getStatus());
            jpa.setExternalTransactionId(payment.getExternalTransactionId());
            jpa.setPaidAt(payment.getPaidAt());
            jpa.setRefundedAt(payment.getRefundedAt());
        } else {
            jpa = new JpaPayment();
            jpa.setOrderId(payment.getOrderId());
            jpa.setOrderNumber(payment.getOrderNumber());
            jpa.setUserId(payment.getUserId());
            jpa.setAmount(payment.getAmount());
            jpa.setMethod(payment.getMethod());
            jpa.setStatus(payment.getStatus());
            jpa.setExternalTransactionId(payment.getExternalTransactionId());
            jpa.setPaidAt(payment.getPaidAt());
            jpa.setRefundedAt(payment.getRefundedAt());
        }

        jpa = jpaPaymentRepository.save(jpa);
        return mapper.toDomain(jpa);
    }

    @Override
    public Optional<Payment> findByOrderId(Long orderId) {
        return jpaPaymentRepository.findByOrderId(orderId).map(mapper::toDomain);
    }
}
