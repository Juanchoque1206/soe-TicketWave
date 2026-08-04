package com.soe.jcb.eventdriven.demo.payment.infrastructure.gateway;

import com.soe.jcb.eventdriven.demo.payment.application.out.PaymentGatewayPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.UUID;

/**
 * Stub gateway adapter for local development; always succeeds. Swap this bean
 * for a real provider adapter in production.
 */
@Component
public class StubPaymentGatewayAdapter implements PaymentGatewayPort {

    private static final Logger log = LoggerFactory.getLogger(StubPaymentGatewayAdapter.class);

    @Override
    public PaymentGatewayResult charge(BigDecimal amount, String method) {
        log.info("STUB payment gateway charging {} via {}", amount, method);
        return new PaymentGatewayResult(true, "STUB-" + UUID.randomUUID());
    }
}