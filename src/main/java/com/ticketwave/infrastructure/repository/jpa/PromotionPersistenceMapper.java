package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.promotion.model.Promotion;
import org.springframework.stereotype.Component;

@Component
public class PromotionPersistenceMapper {

    public Promotion toDomain(JpaPromotion jpa) {
        Promotion p = new Promotion();
        p.setId(jpa.getId());
        p.setCode(jpa.getCode());
        p.setDescription(jpa.getDescription());
        p.setScope(jpa.getScope());
        p.setVenueId(jpa.getVenue() != null ? jpa.getVenue().getId() : null);
        p.setDiscountPercentage(jpa.getDiscountPercentage());
        p.setDiscountFlat(jpa.getDiscountFlat());
        p.setMaxUses(jpa.getMaxUses());
        p.setCurrentUses(jpa.getCurrentUses());
        p.setValidFrom(jpa.getValidFrom());
        p.setValidUntil(jpa.getValidUntil());
        p.setActive(jpa.isActive());
        p.setCreatedAt(jpa.getCreatedAt());
        p.setUpdatedAt(jpa.getUpdatedAt());
        return p;
    }

    public JpaPromotion toJpa(Promotion domain) {
        JpaPromotion jpa = new JpaPromotion();
        jpa.setId(domain.getId());
        jpa.setCode(domain.getCode());
        jpa.setDescription(domain.getDescription());
        jpa.setScope(domain.getScope());
        jpa.setDiscountPercentage(domain.getDiscountPercentage());
        jpa.setDiscountFlat(domain.getDiscountFlat());
        jpa.setMaxUses(domain.getMaxUses());
        jpa.setCurrentUses(domain.getCurrentUses());
        jpa.setValidFrom(domain.getValidFrom());
        jpa.setValidUntil(domain.getValidUntil());
        jpa.setActive(domain.isActive());
        return jpa;
    }
}
