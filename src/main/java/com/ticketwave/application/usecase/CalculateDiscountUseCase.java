package com.ticketwave.application.usecase;

import com.ticketwave.domain.promotion.model.Promotion;
import java.math.BigDecimal;

public interface CalculateDiscountUseCase {
    BigDecimal calculateDiscount(Promotion promotion, BigDecimal totalAmount);
}
