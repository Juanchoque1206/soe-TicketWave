package com.soe.jcb.eventdriven.demo.promotion.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.promotion.application.out.VenueExistsChecker;
import com.soe.jcb.eventdriven.demo.promotion.domain.Promotion;
import com.soe.jcb.eventdriven.demo.promotion.domain.PromotionRepository;
import com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence.VenueJpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class PromotionRepositoryJpaAdapter implements PromotionRepository {

    private final PromotionJpaRepository jpaRepository;

    public PromotionRepositoryJpaAdapter(PromotionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Promotion save(Promotion promotion) {
        PromotionJpaEntity entity = toJpa(promotion);
        PromotionJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Promotion> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Promotion> findByCodeAndActiveTrue(String code) {
        return jpaRepository.findByCodeAndActiveTrue(code).map(this::toDomain);
    }

    @Override
    public List<Promotion> findByActiveTrue() {
        return jpaRepository.findByActiveTrue().stream().map(this::toDomain).toList();
    }

    @Override
    @Transactional
    public boolean incrementCurrentUses(Long id) {
        return jpaRepository.incrementCurrentUses(id) > 0;
    }

    private PromotionJpaEntity toJpa(Promotion promotion) {
        PromotionJpaEntity entity = new PromotionJpaEntity();
        entity.setId(promotion.getId());
        entity.setCode(promotion.getCode());
        entity.setDescription(promotion.getDescription());
        entity.setScope(promotion.getScope());
        entity.setVenueId(promotion.getVenueId());
        entity.setDiscountPercentage(promotion.getDiscountPercentage());
        entity.setDiscountFlat(promotion.getDiscountFlat());
        entity.setMaxUses(promotion.getMaxUses());
        entity.setCurrentUses(promotion.getCurrentUses());
        entity.setValidFrom(promotion.getValidFrom());
        entity.setValidUntil(promotion.getValidUntil());
        entity.setActive(promotion.isActive());
        return entity;
    }

    private Promotion toDomain(PromotionJpaEntity entity) {
        return new Promotion(entity.getId(), entity.getCode(), entity.getDescription(),
                entity.getScope(), entity.getVenueId(), entity.getDiscountPercentage(),
                entity.getDiscountFlat(), entity.getMaxUses(), entity.getCurrentUses(),
                entity.getValidFrom(), entity.getValidUntil(), entity.isActive());
    }
}