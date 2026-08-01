package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.PromotionResponse;

public interface ValidatePromotionUseCase {
    PromotionResponse validate(String code, Long venueId);
}
