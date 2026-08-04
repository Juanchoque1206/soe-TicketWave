package com.soe.jcb.eventdriven.demo.payment.interfaces.dto;

import com.soe.jcb.eventdriven.demo.payment.application.in.PaymentUseCase;
import com.soe.jcb.eventdriven.demo.payment.domain.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull PaymentMethod method
) {
    public PaymentUseCase.ProcessPaymentCommand toCommand() {
        return new PaymentUseCase.ProcessPaymentCommand(method);
    }
}