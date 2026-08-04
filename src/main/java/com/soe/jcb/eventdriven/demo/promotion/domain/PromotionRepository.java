package com.soe.jcb.eventdriven.demo.promotion.domain;

import java.util.List;
import java.util.Optional;

/**
 * Domain port for persisting {@link Promotion}.
 */
public interface PromotionRepository {

    Promotion save(Promotion promotion);

    Optional<Promotion> findById(Long id);

    Optional<Promotion> findByCodeAndActiveTrue(String code);

    List<Promotion> findByActiveTrue();

    /**
     * Atomically increments currentUses, returning true only if capacity allows.
     */
    boolean incrementCurrentUses(Long id);
}