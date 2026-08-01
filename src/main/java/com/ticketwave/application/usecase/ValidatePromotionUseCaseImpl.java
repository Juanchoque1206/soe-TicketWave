package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.PromotionResponse;
import com.ticketwave.application.mapper.PromotionMapper;
import com.ticketwave.domain.promotion.model.Promotion;
import com.ticketwave.domain.promotion.model.PromotionScope;
import com.ticketwave.application.usecase.ValidatePromotionUseCase;
import com.ticketwave.domain.promotion.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ValidatePromotionUseCaseImpl implements ValidatePromotionUseCase {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public ValidatePromotionUseCaseImpl(PromotionRepository promotionRepository,
                                         PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
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
            if (promotion.getVenueId() == null || !promotion.getVenueId().equals(venueId)) {
                throw new BusinessRuleException("Promotion code is not valid for this venue");
            }
        }

        return promotionMapper.toResponse(promotion);
    }
}
