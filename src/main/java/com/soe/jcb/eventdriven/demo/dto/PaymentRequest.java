package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.PaymentMethod;
import jakarta.validation.constraints.NotNull;

public record PaymentRequest(
        @NotNull PaymentMethod method
) {
}
