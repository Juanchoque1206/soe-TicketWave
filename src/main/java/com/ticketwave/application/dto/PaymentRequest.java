package com.ticketwave.application.dto;

import com.ticketwave.domain.payment.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

public record PaymentRequest(
        @NotNull PaymentMethod method
) implements Serializable {
}
