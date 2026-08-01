package com.ticketwave.domain.promotion.model;

import com.ticketwave.domain.common.model.BaseEntity;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Promotion extends BaseEntity {
    private String code;
    private String description;
    private PromotionScope scope;
    private Long venueId;
    private BigDecimal discountPercentage;
    private BigDecimal discountFlat;
    private int maxUses;
    private int currentUses = 0;
    private LocalDateTime validFrom;
    private LocalDateTime validUntil;
    private boolean active = true;

    public Promotion() {}

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public PromotionScope getScope() { return scope; }
    public void setScope(PromotionScope scope) { this.scope = scope; }
    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }
    public BigDecimal getDiscountPercentage() { return discountPercentage; }
    public void setDiscountPercentage(BigDecimal discountPercentage) { this.discountPercentage = discountPercentage; }
    public BigDecimal getDiscountFlat() { return discountFlat; }
    public void setDiscountFlat(BigDecimal discountFlat) { this.discountFlat = discountFlat; }
    public int getMaxUses() { return maxUses; }
    public void setMaxUses(int maxUses) { this.maxUses = maxUses; }
    public int getCurrentUses() { return currentUses; }
    public void setCurrentUses(int currentUses) { this.currentUses = currentUses; }
    public LocalDateTime getValidFrom() { return validFrom; }
    public void setValidFrom(LocalDateTime validFrom) { this.validFrom = validFrom; }
    public LocalDateTime getValidUntil() { return validUntil; }
    public void setValidUntil(LocalDateTime validUntil) { this.validUntil = validUntil; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}
