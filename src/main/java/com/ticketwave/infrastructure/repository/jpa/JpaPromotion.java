package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.infrastructure.persistence.JpaBaseEntity;
import com.ticketwave.domain.promotion.model.PromotionScope;
import com.ticketwave.infrastructure.repository.jpa.JpaVenue;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotions")
public class JpaPromotion extends JpaBaseEntity {

    @Column(nullable = false, unique = true)
    private String code;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PromotionScope scope;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id")
    private JpaVenue venue;

    @Column(name = "discount_percentage", precision = 5, scale = 2)
    private BigDecimal discountPercentage;

    @Column(name = "discount_flat", precision = 10, scale = 2)
    private BigDecimal discountFlat;

    @Column(name = "max_uses", nullable = false)
    private int maxUses;

    @Column(name = "current_uses", nullable = false)
    private int currentUses = 0;

    @Column(name = "valid_from", nullable = false)
    private LocalDateTime validFrom;

    @Column(name = "valid_until", nullable = false)
    private LocalDateTime validUntil;

    @Column(nullable = false)
    private boolean active = true;

    public JpaPromotion() {}

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public PromotionScope getScope() {
        return scope;
    }

    public void setScope(PromotionScope scope) {
        this.scope = scope;
    }

    public JpaVenue getVenue() {
        return venue;
    }

    public void setVenue(JpaVenue venue) {
        this.venue = venue;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(BigDecimal discountPercentage) {
        this.discountPercentage = discountPercentage;
    }

    public BigDecimal getDiscountFlat() {
        return discountFlat;
    }

    public void setDiscountFlat(BigDecimal discountFlat) {
        this.discountFlat = discountFlat;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public void setMaxUses(int maxUses) {
        this.maxUses = maxUses;
    }

    public int getCurrentUses() {
        return currentUses;
    }

    public void setCurrentUses(int currentUses) {
        this.currentUses = currentUses;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public void setValidFrom(LocalDateTime validFrom) {
        this.validFrom = validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public void setValidUntil(LocalDateTime validUntil) {
        this.validUntil = validUntil;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
