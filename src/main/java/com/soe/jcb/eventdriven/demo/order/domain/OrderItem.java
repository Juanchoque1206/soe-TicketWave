package com.soe.jcb.eventdriven.demo.order.domain;

import java.math.BigDecimal;

/**
 * OrderItem value/entity within the Order aggregate. Pure domain - no JPA.
 */
public class OrderItem {

    private Long id;
    private Long ticketTypeId;
    private String ticketTypeName;
    private BigDecimal unitPrice;
    private Long seatId;
    private String seatRow;
    private Integer seatNumber;

    public OrderItem(Long id, Long ticketTypeId, String ticketTypeName, BigDecimal unitPrice,
                     Long seatId, String seatRow, Integer seatNumber) {
        this.id = id;
        this.ticketTypeId = ticketTypeId;
        this.ticketTypeName = ticketTypeName;
        this.unitPrice = unitPrice;
        this.seatId = seatId;
        this.seatRow = seatRow;
        this.seatNumber = seatNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTicketTypeId() {
        return ticketTypeId;
    }

    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Long getSeatId() {
        return seatId;
    }

    public String getSeatRow() {
        return seatRow;
    }

    public Integer getSeatNumber() {
        return seatNumber;
    }
}