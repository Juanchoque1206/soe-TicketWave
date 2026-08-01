package com.ticketwave.domain.antifraud.service;

import com.ticketwave.domain.antifraud.model.FraudCheckResult;

public class FraudDetectionService {

    private static final int MAX_PENDING_ORDERS_PER_USER = 5;
    private static final int FRAUD_CHECK_MINUTES = 10;

    public FraudCheckResult checkOrderFraud(long pendingOrderCount) {
        if (pendingOrderCount >= MAX_PENDING_ORDERS_PER_USER) {
            return FraudCheckResult.detected(
                    "Too many pending orders. Please complete or cancel existing orders.");
        }
        return FraudCheckResult.clean();
    }

    public int getFraudCheckMinutes() {
        return FRAUD_CHECK_MINUTES;
    }
}
