package com.ticketwave.application.usecase;

import com.ticketwave.domain.promotion.model.Promotion;
import com.ticketwave.application.usecase.CalculateDiscountUseCase;
import com.ticketwave.domain.promotion.service.DiscountCalculator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CalculateDiscountUseCaseImpl implements CalculateDiscountUseCase {

    private final DiscountCalculator discountCalculator;

    public CalculateDiscountUseCaseImpl(DiscountCalculator discountCalculator) {
        this.discountCalculator = discountCalculator;
    }

    @Override
    public BigDecimal calculateDiscount(Promotion promotion, BigDecimal totalAmount) {
        return discountCalculator.calculateDiscount(promotion, totalAmount);
    }
}
