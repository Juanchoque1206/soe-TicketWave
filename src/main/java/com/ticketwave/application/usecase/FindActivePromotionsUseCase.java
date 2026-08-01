package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.PromotionResponse;
import java.util.List;

public interface FindActivePromotionsUseCase {
    List<PromotionResponse> findActive();
}
