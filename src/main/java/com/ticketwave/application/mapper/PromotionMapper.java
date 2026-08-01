package com.ticketwave.application.mapper;

import com.ticketwave.application.dto.PromotionResponse;
import com.ticketwave.domain.promotion.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionMapper {

    public PromotionResponse toResponse(Promotion promotion) {
        return new PromotionResponse(
                promotion.getId(),
                promotion.getCode(),
                promotion.getDescription(),
                promotion.getScope(),
                promotion.getVenueId(),
                promotion.getDiscountPercentage(),
                promotion.getDiscountFlat(),
                promotion.getMaxUses(),
                promotion.getCurrentUses(),
                promotion.getValidFrom(),
                promotion.getValidUntil(),
                promotion.isActive());
    }
}
