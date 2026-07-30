package com.soe.jcb.eventdriven.demo.promotion.dto;

import com.soe.jcb.eventdriven.demo.promotion.entity.PromotionScope;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionRequest(
        @NotBlank String code,
        String description,
        @NotNull PromotionScope scope,
        Long venueId,
        BigDecimal discountPercentage,
        BigDecimal discountFlat,
        int maxUses,
        @NotNull LocalDateTime validFrom,
        @NotNull LocalDateTime validUntil
) {
}
