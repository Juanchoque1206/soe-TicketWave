package com.soe.jcb.eventdriven.demo.order.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Order aggregate root (pure domain - no annotations).
 *
 * <p>Cross-context references are kept as ids (userId, seat ids, ticket-type ids)
 * so the order context stays decoupled from user/venue/event domains.
 */
public class Order {

    private Long id;
    private String orderNumber;
    private Long userId;
    private OrderStatus status = OrderStatus.PENDING;
    private BigDecimal totalAmount = BigDecimal.ZERO;
    private String promotionCode;
    private BigDecimal discountAmount = BigDecimal.ZERO;
    private LocalDateTime createdAt;
    private final List<OrderItem> items = new ArrayList<>();

    public Order(Long id, String orderNumber, Long userId, OrderStatus status, BigDecimal totalAmount,
                 String promotionCode, BigDecimal discountAmount, LocalDateTime createdAt) {
        this.id = id;
        this.orderNumber = orderNumber;
        this.userId = userId;
        this.status = status;
        this.totalAmount = totalAmount;
        this.promotionCode = promotionCode;
        this.discountAmount = discountAmount;
        this.createdAt = createdAt;
    }

    public void addItem(OrderItem item) {
        items.add(item);
    }

    public void confirm() {
        if (status != OrderStatus.PENDING) {
            throw new IllegalStateException("Order cannot be confirmed in status: " + status);
        }
        status = OrderStatus.CONFIRMED;
    }

    public void cancel() {
        if (status != OrderStatus.PENDING && status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Order cannot be cancelled in status: " + status);
        }
        status = OrderStatus.CANCELLED;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<OrderItem> getItems() {
        return items;
    }
}