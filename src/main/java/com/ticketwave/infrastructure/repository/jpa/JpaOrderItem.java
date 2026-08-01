package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.infrastructure.persistence.JpaBaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class JpaOrderItem extends JpaBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private JpaOrder order;

    @Column(name = "ticket_type_id", nullable = false)
    private Long ticketTypeId;

    @Column(name = "ticket_type_name")
    private String ticketTypeName;

    @Column(name = "unit_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "seat_id")
    private Long seatId;

    @Column(name = "seat_row")
    private String seatRow;

    @Column(name = "seat_number")
    private Integer seatNumber;

    public JpaOrderItem() {
    }

    public JpaOrder getOrder() {
        return order;
    }

    public void setOrder(JpaOrder order) {
        this.order = order;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public void setTicketTypeId(Long ticketTypeId) {
        this.ticketTypeId = ticketTypeId;
    }

    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public Long getSeatId() {
        return seatId;
    }

    public void setSeatId(Long seatId) {
        this.seatId = seatId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public void setSeatRow(String seatRow) {
        this.seatRow = seatRow;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }

    public void setSeatNumber(Integer seatNumber) {
        this.seatNumber = seatNumber;
    }
}
