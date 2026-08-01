package com.ticketwave.domain.promotion.repository;

import com.ticketwave.domain.promotion.model.Promotion;
import java.util.List;
import java.util.Optional;

public interface PromotionRepository {
    Promotion save(Promotion promotion);
    Optional<Promotion> findByCodeAndActiveTrue(String code);
    List<Promotion> findByActiveTrue();
    int incrementCurrentUses(Long id);
}
