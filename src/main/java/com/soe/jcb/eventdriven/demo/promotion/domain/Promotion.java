package com.soe.jcb.eventdriven.demo.promotion.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Promotion aggregate root (pure domain - no annotations).
 *
 * <p>References the venue by id (venueId) to keep the bounded context
 * decoupled from the venue context.
 */
public class Promotion {

    private Long id;
    private String code;
    private String description;
    private PromotionScope scope;
    private Long venueId;
    private BigDecimal discountPercentage;
    private BigDecimal discountFlat;
    private int maxUses;
    private int currentUses;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private boolean active;

    public Promotion(Long id, String code, String description, PromotionScope scope, Long venueId,
                     BigDecimal discountPercentage, BigDecimal discountFlat, int maxUses, int currentUses,
                     LocalDateTime validFrom, LocalDateTime validUntil, boolean active) {
        this.id = id;
        this.code = code;
        this.description = description;
        this.scope = scope;
        this.venueId = venueId;
        this.discountPercentage = discountPercentage;
        this.discountFlat = discountFlat;
        this.maxUses = maxUses;
        this.currentUses = currentUses;
        this.validFrom = validFrom;
        this.validUntil = validUntil;
        this.active = active;
    }

    public Long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public PromotionScope getScope() {
        return scope;
    }

    public Long getVenueId() {
        return venueId;
    }

    public BigDecimal getDiscountPercentage() {
        return discountPercentage;
    }

    public BigDecimal getDiscountFlat() {
        return discountFlat;
    }

    public int getMaxUses() {
        return maxUses;
    }

    public int getCurrentUses() {
        return currentUses;
    }

    public LocalDateTime getValidFrom() {
        return validFrom;
    }

    public LocalDateTime getValidUntil() {
        return validUntil;
    }

    public boolean isActive() {
        return active;
    }
}