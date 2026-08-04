package com.soe.jcb.eventdriven.demo.payment.application.out;

import java.math.BigDecimal;

/**
 * Outbound port to an external payment gateway. Implemented by a stub for
 * local development and by a real provider adapter in production.
 */
public interface PaymentGatewayPort {

    PaymentGatewayResult charge(BigDecimal amount, String method);

    record PaymentGatewayResult(boolean success, String externalTransactionId) {
    }
}