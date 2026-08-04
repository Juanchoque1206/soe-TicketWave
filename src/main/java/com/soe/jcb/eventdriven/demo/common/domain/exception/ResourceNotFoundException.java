package com.soe.jcb.eventdriven.demo.common.domain.exception;

/**
 * Raised when an aggregate cannot be located by the given lookup key.
 *
 * <p>Framework-independent; the web adapter in {@code common.infrastructure}
 * maps it to HTTP 404.
 */
public class ResourceNotFoundException extends RuntimeException {

    private final String entityName;
    private final String fieldName;
    private final Object fieldValue;

    public ResourceNotFoundException(String entityName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", entityName, fieldName, fieldValue));
        this.entityName = entityName;
        this.fieldName = fieldName;
        this.fieldValue = fieldValue;
    }

    public String getEntityName() {
        return entityName;
    }

    public String getFieldName() {
        return fieldName;
    }

    public Object getFieldValue() {
        return fieldValue;
    }
}
