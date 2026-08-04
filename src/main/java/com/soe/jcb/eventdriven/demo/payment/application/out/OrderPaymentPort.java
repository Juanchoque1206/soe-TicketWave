package com.soe.jcb.eventdriven.demo.payment.application.out;

import java.math.BigDecimal;

/**
 * Anti-corruption port exposing just enough of the order context for payment
 * processing/refunds, without importing order domain types.
 */
public interface OrderPaymentPort {

    OrderSnapshot findByOrderNumber(String orderNumber);

    boolean confirmOrder(Long orderId);

    record OrderSnapshot(Long id, String orderNumber, String status, BigDecimal totalAmount) {
    }
}