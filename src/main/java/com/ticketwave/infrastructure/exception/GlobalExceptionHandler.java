package com.ticketwave.infrastructure.exception;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.DuplicatePurchaseException;
import com.ticketwave.domain.common.exception.FraudDetectedException;
import com.ticketwave.domain.common.exception.InsufficientTicketsException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiResponse<Void> handleNotFound(ResourceNotFoundException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(BusinessRuleException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ApiResponse<Void> handleBusinessRule(BusinessRuleException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(DuplicatePurchaseException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleDuplicate(DuplicatePurchaseException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(InsufficientTicketsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiResponse<Void> handleInsufficientTickets(InsufficientTicketsException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(FraudDetectedException.class)
    @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
    public ApiResponse<Void> handleFraudDetected(FraudDetectedException ex) {
        return ApiResponse.error(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse<Map<String, String>> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));
        return new ApiResponse<>(false, "Validation failed", errors);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiResponse<Void> handleAccessDenied(AuthorizationDeniedException ex) {
        return ApiResponse.error("Access denied");
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse<Void> handleGeneral(Exception ex) {
        log.error("Unexpected error", ex);
        return ApiResponse.error("An unexpected error occurred");
    }
}
