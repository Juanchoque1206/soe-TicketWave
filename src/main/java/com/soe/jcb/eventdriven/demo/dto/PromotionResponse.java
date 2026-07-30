package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Promotion;
import com.soe.jcb.eventdriven.demo.entity.PromotionScope;

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
    public static PromotionResponse from(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getCode(),
                promotion.getDescription(),
                promotion.getScope(),
                promotion.getVenue() != null ? promotion.getVenue().getId() : null,
                promotion.getDiscountPercentage(),
                promotion.getDiscountFlat(),
                promotion.getMaxUses(),
                promotion.getCurrentUses(),
                promotion.getValidFrom(),
                promotion.getValidUntil(),
                promotion.isActive()
        );
    }
}
