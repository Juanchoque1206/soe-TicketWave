package com.soe.jcb.eventdriven.demo.promotion.application.service;

import com.soe.jcb.eventdriven.demo.common.domain.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.common.domain.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.promotion.application.in.PromotionUseCase;
import com.soe.jcb.eventdriven.demo.promotion.application.out.VenueExistsChecker;
import com.soe.jcb.eventdriven.demo.promotion.domain.Promotion;
import com.soe.jcb.eventdriven.demo.promotion.domain.PromotionRepository;
import com.soe.jcb.eventdriven.demo.promotion.domain.PromotionScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionService implements PromotionUseCase {

    private final PromotionRepository promotionRepository;
    private final VenueExistsChecker venueExistsChecker;

    public PromotionService(PromotionRepository promotionRepository, VenueExistsChecker venueExistsChecker) {
        this.promotionRepository = promotionRepository;
        this.venueExistsChecker = venueExistsChecker;
    }

    @Override
    @Transactional
    public PromotionResult create(CreatePromotionCommand command) {
        if (command.scope() == PromotionScope.VENUE) {
            if (command.venueId() == null) {
                throw new BusinessRuleException("Venue ID is required for VENUE-scoped promotions");
            }
            if (!venueExistsChecker.existsById(command.venueId())) {
                throw new ResourceNotFoundException("Venue", "id", command.venueId());
            }
        }
        Promotion promotion = new Promotion(null, command.code().toUpperCase(), command.description(),
                command.scope(), command.venueId(), command.discountPercentage(), command.discountFlat(),
                command.maxUses(), 0, command.validFrom(), command.validUntil(), true);
        return toResult(promotionRepository.save(promotion));
    }

    @Override
    @Transactional(readOnly = true)
    public PromotionResult validate(String code, Long venueId) {
        Promotion promotion = promotionRepository.findByCodeAndActiveTrue(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Promotion", "code", code));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promotion.getValidFrom()) || now.isAfter(promotion.getValidUntil())) {
            throw new BusinessRuleException("Promotion code is not valid at this time");
        }
        if (promotion.getCurrentUses() >= promotion.getMaxUses()) {
            throw new BusinessRuleException("Promotion code has reached its maximum uses");
        }
        if (promotion.getScope() == PromotionScope.VENUE && venueId != null
                && (promotion.getVenueId() == null || !promotion.getVenueId().equals(venueId))) {
            throw new BusinessRuleException("Promotion code is not valid for this venue");
        }
        return toResult(promotion);
    }

    @Override
    @Transactional
    public void redeem(Long promotionId) {
        if (!promotionRepository.incrementCurrentUses(promotionId)) {
            throw new BusinessRuleException("Promotion has reached its maximum uses");
        }
    }

    @Override
    public BigDecimal calculateDiscount(PromotionResult promotion, BigDecimal totalAmount) {
        if (promotion.discountPercentage() != null) {
            return totalAmount.multiply(promotion.discountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (promotion.discountFlat() != null) {
            return promotion.discountFlat().min(totalAmount);
        }
        return BigDecimal.ZERO;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResult> findActive() {
        return promotionRepository.findByActiveTrue().stream().map(this::toResult).toList();
    }

    private PromotionResult toResult(Promotion promotion) {
        return new PromotionResult(promotion.getId(), promotion.getCode(), promotion.getDescription(),
                promotion.getScope(), promotion.getVenueId(), promotion.getDiscountPercentage(),
                promotion.getDiscountFlat(), promotion.getMaxUses(), promotion.getCurrentUses(),
                promotion.getValidFrom(), promotion.getValidUntil(), promotion.isActive());
    }
}