package com.ticketwave.domain.payment.repository;

import com.ticketwave.domain.payment.model.PaymentMethod;

import java.math.BigDecimal;

public interface PaymentGateway {

    PaymentResult charge(Long orderId, String orderNumber, BigDecimal amount, PaymentMethod method);

    PaymentResult refund(Long paymentId, String orderNumber, BigDecimal amount);
}
