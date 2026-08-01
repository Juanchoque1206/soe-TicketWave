package com.ticketwave.domain.common.exception;

public class DuplicatePurchaseException extends RuntimeException {

    public DuplicatePurchaseException(String message) {
        super(message);
    }
}
