package com.soe.jcb.eventdriven.demo.common.domain.exception;

/**
 * Raised when a seat is already reserved for an active order of the same event.
 *
 * <p>Mapped to HTTP 409 by the web adapter.
 */
public class DuplicatePurchaseException extends RuntimeException {

    public DuplicatePurchaseException(String message) {
        super(message);
    }
}
