package com.ticketwave.domain.promotion.service;

import com.ticketwave.domain.promotion.model.Promotion;
import java.math.BigDecimal;
import java.math.RoundingMode;

public class DiscountCalculator {

    public BigDecimal calculateDiscount(Promotion promotion, BigDecimal totalAmount) {
        if (promotion.getDiscountPercentage() != null) {
            return totalAmount.multiply(promotion.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (promotion.getDiscountFlat() != null) {
            return promotion.getDiscountFlat().min(totalAmount);
        }
        return BigDecimal.ZERO;
    }
}
