package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.application.dto.PromotionRequest;
import com.ticketwave.application.dto.PromotionResponse;
import com.ticketwave.application.mapper.PromotionMapper;
import com.ticketwave.domain.promotion.model.Promotion;
import com.ticketwave.domain.promotion.model.PromotionScope;
import com.ticketwave.application.usecase.CreatePromotionUseCase;
import com.ticketwave.domain.promotion.repository.PromotionRepository;
import com.ticketwave.domain.promotion.repository.VenueQueryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreatePromotionUseCaseImpl implements CreatePromotionUseCase {

    private final PromotionRepository promotionRepository;
    private final VenueQueryPort venueQueryPort;
    private final PromotionMapper promotionMapper;

    public CreatePromotionUseCaseImpl(PromotionRepository promotionRepository,
                                       VenueQueryPort venueQueryPort,
                                       PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.venueQueryPort = venueQueryPort;
        this.promotionMapper = promotionMapper;
    }

    @Override
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
            venueQueryPort.findVenueById(request.venueId());
            promotion.setVenueId(request.venueId());
        }

        promotion = promotionRepository.save(promotion);
        return promotionMapper.toResponse(promotion);
    }
}
