package com.ticketwave.application.dto;

import com.ticketwave.domain.promotion.model.PromotionScope;
import java.io.Serializable;
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
) implements Serializable {}
