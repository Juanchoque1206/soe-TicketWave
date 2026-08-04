package com.soe.jcb.eventdriven.demo.promotion.interfaces.dto;

import com.soe.jcb.eventdriven.demo.promotion.application.in.PromotionUseCase;
import com.soe.jcb.eventdriven.demo.promotion.domain.PromotionScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionResponse(
        Long id,
        String code,
        String description,
        PromotionScope scope,
        Long venueId,
        BigDecimal discountPercentage,
        BigDecimal discountFlat,
        int maxUses,
        int currentUses,
        LocalDateTime validFrom,
        LocalDateTime validUntil,
        boolean active
) {
    public static PromotionResponse from(PromotionUseCase.PromotionResult result) {
        return new PromotionResponse(result.id(), result.code(), result.description(), result.scope(),
                result.venueId(), result.discountPercentage(), result.discountFlat(), result.maxUses(),
                result.currentUses(), result.validFrom(), result.validUntil(), result.active());
    }
}