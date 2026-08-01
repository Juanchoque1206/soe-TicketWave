package com.ticketwave.domain.ticket.model;

import com.ticketwave.domain.common.model.BaseEntity;

import java.math.BigDecimal;

public class OrderItem extends BaseEntity {

    private Order order;
    private Long ticketTypeId;
    private String ticketTypeName;
    private BigDecimal unitPrice;
    private Long seatId;
    private String seatRow;
    private Integer seatNumber;

    public OrderItem() {
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
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
