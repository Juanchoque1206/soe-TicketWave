package com.soe.jcb.eventdriven.demo.common.exception;

public class DuplicatePurchaseException extends RuntimeException {

    public DuplicatePurchaseException(String message) {
        super(message);
    }
}
