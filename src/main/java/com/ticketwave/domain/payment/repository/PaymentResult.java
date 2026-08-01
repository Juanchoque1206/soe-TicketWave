package com.ticketwave.domain.payment.repository;

public record PaymentResult(
        boolean success,
        String transactionId,
        String errorMessage
) {
    public static PaymentResult success(String transactionId) {
        return new PaymentResult(true, transactionId, null);
    }

    public static PaymentResult failure(String errorMessage) {
        return new PaymentResult(false, null, errorMessage);
    }
}
