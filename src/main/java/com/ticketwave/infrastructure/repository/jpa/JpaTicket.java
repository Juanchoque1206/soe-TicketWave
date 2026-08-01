package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.infrastructure.persistence.JpaBaseEntity;
import com.ticketwave.domain.ticket.model.TicketStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets", indexes = {
        @Index(name = "idx_ticket_order", columnList = "order_id"),
        @Index(name = "idx_ticket_status", columnList = "status")
})
public class JpaTicket extends JpaBaseEntity {

    @Column(name = "ticket_code", nullable = false, unique = true)
    private String ticketCode;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "order_item_id", nullable = false, unique = true)
    private Long orderItemId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.VALID;

    @Column(name = "issued_at", nullable = false)
    private LocalDateTime issuedAt;

    @Column(name = "validated_at")
    private LocalDateTime validatedAt;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "ticket_type_name")
    private String ticketTypeName;

    @Column(name = "seat_row")
    private String seatRow;

    @Column(name = "seat_number")
    private Integer seatNumber;

    public JpaTicket() {
    }

    public String getTicketCode() {
        return ticketCode;
    }

    public void setTicketCode(String ticketCode) {
        this.ticketCode = ticketCode;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public TicketStatus getStatus() {
        return status;
    }

    public void setStatus(TicketStatus status) {
        this.status = status;
    }

    public LocalDateTime getIssuedAt() {
        return issuedAt;
    }

    public void setIssuedAt(LocalDateTime issuedAt) {
        this.issuedAt = issuedAt;
    }

    public LocalDateTime getValidatedAt() {
        return validatedAt;
    }

    public void setValidatedAt(LocalDateTime validatedAt) {
        this.validatedAt = validatedAt;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getVenueName() {
        return venueName;
    }

    public void setVenueName(String venueName) {
        this.venueName = venueName;
    }

    public String getTicketTypeName() {
        return ticketTypeName;
    }

    public void setTicketTypeName(String ticketTypeName) {
        this.ticketTypeName = ticketTypeName;
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
