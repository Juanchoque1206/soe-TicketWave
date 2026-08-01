package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentPersistenceMapper {

    public Payment toDomain(JpaPayment jpa) {
        Payment payment = new Payment();
        payment.setId(jpa.getId());
        payment.setOrderId(jpa.getOrderId());
        payment.setOrderNumber(jpa.getOrderNumber());
        payment.setUserId(jpa.getUserId());
        payment.setAmount(jpa.getAmount());
        payment.setMethod(jpa.getMethod());
        payment.setStatus(jpa.getStatus());
        payment.setExternalTransactionId(jpa.getExternalTransactionId());
        payment.setPaidAt(jpa.getPaidAt());
        payment.setRefundedAt(jpa.getRefundedAt());
        payment.setCreatedAt(jpa.getCreatedAt());
        payment.setUpdatedAt(jpa.getUpdatedAt());
        return payment;
    }
}
