package com.soe.jcb.eventdriven.demo.event.domain;

import java.math.BigDecimal;

/**
 * TicketType - a child entity within the Event aggregate. Pure domain.
 */
public class TicketType {

    private Long id;
    private String name;
    private BigDecimal price;
    private int totalQuantity;
    private int soldQuantity;
    private Long sectionId;

    public TicketType(Long id, String name, BigDecimal price, int totalQuantity,
                      int soldQuantity, Long sectionId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.totalQuantity = totalQuantity;
        this.soldQuantity = soldQuantity;
        this.sectionId = sectionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public int getSoldQuantity() {
        return soldQuantity;
    }

    public void setSoldQuantity(int soldQuantity) {
        this.soldQuantity = soldQuantity;
    }

    public int getAvailableQuantity() {
        return totalQuantity - soldQuantity;
    }

    public Long getSectionId() {
        return sectionId;
    }
}