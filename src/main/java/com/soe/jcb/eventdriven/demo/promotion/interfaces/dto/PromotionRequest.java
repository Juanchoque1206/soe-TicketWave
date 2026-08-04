package com.soe.jcb.eventdriven.demo.promotion.interfaces.dto;

import com.soe.jcb.eventdriven.demo.promotion.application.in.PromotionUseCase;
import com.soe.jcb.eventdriven.demo.promotion.domain.PromotionScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PromotionRequest(
        String code,
        String description,
        PromotionScope scope,
        Long venueId,
        BigDecimal discountPercentage,
        BigDecimal discountFlat,
        int maxUses,
        LocalDateTime validFrom,
        LocalDateTime validUntil
) {
    public PromotionUseCase.CreatePromotionCommand toCommand() {
        return new PromotionUseCase.CreatePromotionCommand(code, description, scope, venueId,
                discountPercentage, discountFlat, maxUses, validFrom, validUntil);
    }
}