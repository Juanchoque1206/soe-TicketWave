package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.infrastructure.persistence.JpaBaseEntity;
import com.ticketwave.infrastructure.repository.jpa.JpaSection;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "ticket_types")
public class JpaTicketType extends JpaBaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "total_quantity", nullable = false)
    private int totalQuantity;

    @Column(name = "sold_quantity", nullable = false)
    private int soldQuantity = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id", nullable = false)
    private JpaEvent event;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private JpaSection section;

    public JpaTicketType() {}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
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

    public JpaEvent getEvent() {
        return event;
    }

    public void setEvent(JpaEvent event) {
        this.event = event;
    }

    public JpaSection getSection() {
        return section;
    }

    public void setSection(JpaSection section) {
        this.section = section;
    }
}
