package com.soe.jcb.eventdriven.demo.payment.dto;

import com.soe.jcb.eventdriven.demo.payment.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull PaymentMethod method
) {
}
