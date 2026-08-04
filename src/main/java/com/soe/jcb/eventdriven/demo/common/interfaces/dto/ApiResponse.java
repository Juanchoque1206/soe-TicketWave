package com.soe.jcb.eventdriven.demo.common.interfaces.dto;

/**
 * Generic REST envelope used by interface (web) adapters.
 *
 * <p>This is a web-layer DTO in {@code common.interfaces} — it is only ever
 * produced/consumed by the interfaces layer, never by domain or application.
 */
public record ApiResponse<T>(
        boolean success,
        String message,
        T data
) {
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(true, "Success", data);
    }

    public static <T> ApiResponse<T> ok(String message, T data) {
        return new ApiResponse<>(true, message, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(false, message, null);
    }
}