package com.soe.jcb.eventdriven.demo.order.application.out;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Anti-corruption port exposing just enough of the promotion context for
 * validating and applying promotion codes at order time.
 */
public interface PromotionOrderPort {

    Optional<PromotionSnapshot> findByCodeAndActive(String code);

    boolean incrementUses(Long id);

    record PromotionSnapshot(Long id, String code, BigDecimal discountPercentage,
                             BigDecimal discountFlat, Long venueId) {
    }
}