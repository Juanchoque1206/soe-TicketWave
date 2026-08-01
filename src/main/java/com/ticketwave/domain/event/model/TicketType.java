package com.ticketwave.domain.event.model;

import com.ticketwave.domain.common.model.BaseEntity;
import java.math.BigDecimal;

public class TicketType extends BaseEntity {
    private String name;
    private BigDecimal price;
    private int totalQuantity;
    private int soldQuantity = 0;
    private Long eventId;
    private Long sectionId;
    private String sectionName;

    public TicketType() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getTotalQuantity() { return totalQuantity; }
    public void setTotalQuantity(int totalQuantity) { this.totalQuantity = totalQuantity; }
    public int getSoldQuantity() { return soldQuantity; }
    public void setSoldQuantity(int soldQuantity) { this.soldQuantity = soldQuantity; }
    public int getAvailableQuantity() { return totalQuantity - soldQuantity; }
    public Long getEventId() { return eventId; }
    public void setEventId(Long eventId) { this.eventId = eventId; }
    public Long getSectionId() { return sectionId; }
    public void setSectionId(Long sectionId) { this.sectionId = sectionId; }
    public String getSectionName() { return sectionName; }
    public void setSectionName(String sectionName) { this.sectionName = sectionName; }
}
