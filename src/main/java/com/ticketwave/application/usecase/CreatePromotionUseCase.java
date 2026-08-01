package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.PromotionRequest;
import com.ticketwave.application.dto.PromotionResponse;

public interface CreatePromotionUseCase {
    PromotionResponse create(PromotionRequest request);
}
