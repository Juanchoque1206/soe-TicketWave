package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.application.usecase.RedeemPromotionUseCase;
import com.ticketwave.domain.promotion.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RedeemPromotionUseCaseImpl implements RedeemPromotionUseCase {

    private final PromotionRepository promotionRepository;

    public RedeemPromotionUseCaseImpl(PromotionRepository promotionRepository) {
        this.promotionRepository = promotionRepository;
    }

    @Override
    @Transactional
    public void redeem(Long promotionId) {
        int updated = promotionRepository.incrementCurrentUses(promotionId);
        if (updated == 0) {
            throw new BusinessRuleException("Promotion has reached its maximum uses");
        }
    }
}
