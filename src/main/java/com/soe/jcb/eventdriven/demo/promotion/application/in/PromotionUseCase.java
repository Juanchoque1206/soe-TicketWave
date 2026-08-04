package com.soe.jcb.eventdriven.demo.promotion.application.in;

import com.soe.jcb.eventdriven.demo.promotion.domain.PromotionScope;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use-case boundary for promotion management (pure, no Spring).
 */
public interface PromotionUseCase {

    PromotionResult create(CreatePromotionCommand command);

    PromotionResult validate(String code, Long venueId);

    List<PromotionResult> findActive();

    void redeem(Long promotionId);

    BigDecimal calculateDiscount(PromotionResult promotion, BigDecimal totalAmount);

    record CreatePromotionCommand(String code, String description, PromotionScope scope,
                                  Long venueId, BigDecimal discountPercentage, BigDecimal discountFlat,
                                  int maxUses, LocalDateTime validFrom, LocalDateTime validUntil) {
    }

    record PromotionResult(Long id, String code, String description, PromotionScope scope,
                           Long venueId, BigDecimal discountPercentage, BigDecimal discountFlat,
                           int maxUses, int currentUses, LocalDateTime validFrom, LocalDateTime validUntil,
                           boolean active) {
    }
}