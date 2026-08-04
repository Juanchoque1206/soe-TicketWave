package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.order.application.out.PromotionOrderPort;
import com.soe.jcb.eventdriven.demo.promotion.infrastructure.persistence.PromotionJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Adapter bridging the order context's {@link PromotionOrderPort} to the
 * promotion context's persistence.
 */
@Component
public class PromotionOrderPortAdapter implements PromotionOrderPort {

    private final PromotionJpaRepository jpaRepository;

    public PromotionOrderPortAdapter(PromotionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<PromotionSnapshot> findByCodeAndActive(String code) {
        return jpaRepository.findByCodeAndActiveTrue(code)
                .map(p -> new PromotionSnapshot(p.getId(), p.getCode(), p.getDiscountPercentage(),
                        p.getDiscountFlat(), p.getVenueId()));
    }

    @Override
    @Transactional
    public boolean incrementUses(Long id) {
        return jpaRepository.incrementCurrentUses(id) > 0;
    }
}