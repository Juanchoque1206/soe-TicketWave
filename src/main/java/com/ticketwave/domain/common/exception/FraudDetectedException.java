package com.ticketwave.domain.common.exception;

public class FraudDetectedException extends RuntimeException {

    public FraudDetectedException(String message) {
        super(message);
    }
}
