package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.PromotionResponse;
import com.ticketwave.application.mapper.PromotionMapper;
import com.ticketwave.application.usecase.FindActivePromotionsUseCase;
import com.ticketwave.domain.promotion.repository.PromotionRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FindActivePromotionsUseCaseImpl implements FindActivePromotionsUseCase {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public FindActivePromotionsUseCaseImpl(PromotionRepository promotionRepository,
                                            PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<PromotionResponse> findActive() {
        return promotionRepository.findByActiveTrue().stream()
                .map(promotionMapper::toResponse)
                .toList();
    }
}
