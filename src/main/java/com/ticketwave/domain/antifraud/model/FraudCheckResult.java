package com.ticketwave.domain.antifraud.model;

public record FraudCheckResult(
        boolean fraudDetected,
        String reason
) {
    public static FraudCheckResult clean() {
        return new FraudCheckResult(false, null);
    }

    public static FraudCheckResult detected(String reason) {
        return new FraudCheckResult(true, reason);
    }
}
