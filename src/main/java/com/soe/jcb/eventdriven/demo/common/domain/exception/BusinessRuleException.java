package com.soe.jcb.eventdriven.demo.common.domain.exception;

/**
 * A business-rule violation.
 *
 * <p>Lives in the shared {@code common} package because every bounded context
 * maps business failures to the same HTTP semantics. It is a pure Java type
 * (no Spring/JPA dependency) so that domain and application layers can throw
 * it without leaking framework concerns.
 */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
