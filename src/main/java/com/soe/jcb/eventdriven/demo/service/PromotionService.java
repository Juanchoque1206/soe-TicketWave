package com.soe.jcb.eventdriven.demo.service;

import com.soe.jcb.eventdriven.demo.dto.PromotionRequest;
import com.soe.jcb.eventdriven.demo.dto.PromotionResponse;
import com.soe.jcb.eventdriven.demo.entity.Promotion;
import com.soe.jcb.eventdriven.demo.entity.PromotionScope;
import com.soe.jcb.eventdriven.demo.entity.Venue;
import com.soe.jcb.eventdriven.demo.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.repository.PromotionRepository;
import com.soe.jcb.eventdriven.demo.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final VenueRepository venueRepository;

    public PromotionService(PromotionRepository promotionRepository,
                            VenueRepository venueRepository) {
        this.promotionRepository = promotionRepository;
        this.venueRepository = venueRepository;
    }

    @Transactional
    public PromotionResponse create(PromotionRequest request) {
        Promotion promotion = new Promotion();
        promotion.setCode(request.code().toUpperCase());
        promotion.setDescription(request.description());
        promotion.setScope(request.scope());
        promotion.setDiscountPercentage(request.discountPercentage());
        promotion.setDiscountFlat(request.discountFlat());
        promotion.setMaxUses(request.maxUses());
        promotion.setValidFrom(request.validFrom());
        promotion.setValidUntil(request.validUntil());

        if (request.scope() == PromotionScope.VENUE) {
            if (request.venueId() == null) {
                throw new BusinessRuleException("Venue ID is required for VENUE-scoped promotions");
            }
            Venue venue = venueRepository.findById(request.venueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", request.venueId()));
            promotion.setVenue(venue);
        }

        promotion = promotionRepository.save(promotion);
        return PromotionResponse.from(promotion);
    }

    @Transactional(readOnly = true)
    public PromotionResponse validate(String code, Long venueId) {
        Promotion promotion = promotionRepository.findByCodeAndActiveTrue(code.toUpperCase())
                .orElseThrow(() -> new ResourceNotFoundException("Promotion", "code", code));

        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(promotion.getValidFrom()) || now.isAfter(promotion.getValidUntil())) {
            throw new BusinessRuleException("Promotion code is not valid at this time");
        }

        if (promotion.getCurrentUses() >= promotion.getMaxUses()) {
            throw new BusinessRuleException("Promotion code has reached its maximum uses");
        }

        if (promotion.getScope() == PromotionScope.VENUE && venueId != null) {
            if (promotion.getVenue() == null || !promotion.getVenue().getId().equals(venueId)) {
                throw new BusinessRuleException("Promotion code is not valid for this venue");
            }
        }

        return PromotionResponse.from(promotion);
    }

    @Transactional
    public void redeem(Long promotionId) {
        int updated = promotionRepository.incrementCurrentUses(promotionId);
        if (updated == 0) {
            throw new BusinessRuleException("Promotion has reached its maximum uses");
        }
    }

    public BigDecimal calculateDiscount(Promotion promotion, BigDecimal totalAmount) {
        if (promotion.getDiscountPercentage() != null) {
            return totalAmount.multiply(promotion.getDiscountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        } else if (promotion.getDiscountFlat() != null) {
            return promotion.getDiscountFlat().min(totalAmount);
        }
        return BigDecimal.ZERO;
    }

    @Transactional(readOnly = true)
    public List<PromotionResponse> findActive() {
        return promotionRepository.findByActiveTrue().stream()
                .map(PromotionResponse::from)
                .toList();
    }
}
