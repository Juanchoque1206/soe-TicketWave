package com.ticketwave.infrastructure.adapter.payment;

import com.ticketwave.domain.payment.model.PaymentMethod;
import com.ticketwave.domain.payment.repository.PaymentGateway;
import com.ticketwave.domain.payment.repository.PaymentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

@Component
public class StubPaymentGateway implements PaymentGateway {

    private static final Logger log = LoggerFactory.getLogger(StubPaymentGateway.class);

    @Override
    public PaymentResult charge(Long orderId, String orderNumber, BigDecimal amount, PaymentMethod method) {
        String transactionId = "STUB-" + UUID.randomUUID();
        log.info("Stub charge approved for order {}: amount={}, method={}, transactionId={}",
                orderNumber, amount, method, transactionId);
        return PaymentResult.success(transactionId);
    }

    @Override
    public PaymentResult refund(Long paymentId, String orderNumber, BigDecimal amount) {
        String transactionId = "STUB-REFUND-" + UUID.randomUUID();
        log.info("Stub refund processed for order {}: amount={}, transactionId={}",
                orderNumber, amount, transactionId);
        return PaymentResult.success(transactionId);
    }
}
