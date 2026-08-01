package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.infrastructure.persistence.JpaBaseEntity;
import com.ticketwave.domain.ticket.model.OrderStatus;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders", indexes = {
        @Index(name = "idx_order_user", columnList = "user_id"),
        @Index(name = "idx_order_status", columnList = "status")
})
public class JpaOrder extends JpaBaseEntity {

    @Column(name = "order_number", nullable = false, unique = true)
    private String orderNumber;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;

    @Column(name = "event_id", nullable = false)
    private Long eventId;

    @Column(name = "event_name")
    private String eventName;

    @Column(name = "event_date")
    private LocalDateTime eventDate;

    @Column(name = "venue_name")
    private String venueName;

    @Column(name = "total_amount", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "promotion_code")
    private String promotionCode;

    @Column(name = "discount_amount", precision = 10, scale = 2)
    private BigDecimal discountAmount;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaOrderItem> items = new ArrayList<>();

    public JpaOrder() {
    }

    public void addItem(JpaOrderItem item) {
        item.setOrder(this);
        this.items.add(item);
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
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

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public BigDecimal getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(BigDecimal discountAmount) {
        this.discountAmount = discountAmount;
    }

    public List<JpaOrderItem> getItems() {
        return items;
    }

    public void setItems(List<JpaOrderItem> items) {
        this.items = items;
    }
}
