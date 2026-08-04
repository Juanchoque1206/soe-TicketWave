package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.order.domain.Order;
import com.soe.jcb.eventdriven.demo.order.domain.OrderItem;
import com.soe.jcb.eventdriven.demo.order.domain.OrderRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class OrderRepositoryJpaAdapter implements OrderRepository {

    private final OrderJpaRepository jpaRepository;

    public OrderRepositoryJpaAdapter(OrderJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public Order save(Order order) {
        OrderJpaEntity entity = toJpa(order);
        OrderJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaRepository.findByOrderNumber(orderNumber).map(this::toDomain);
    }

    @Override
    public List<Order> findByUserIdOrderByCreatedAtDesc(Long userId) {
        return jpaRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public long countPendingSince(Long userId, LocalDateTime after) {
        return jpaRepository.countByUserIdAndStatusAndCreatedAtAfter(
                userId, com.soe.jcb.eventdriven.demo.order.domain.OrderStatus.PENDING, after);
    }

    private OrderJpaEntity toJpa(Order order) {
        OrderJpaEntity entity = new OrderJpaEntity();
        entity.setId(order.getId());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setUserId(order.getUserId());
        entity.setStatus(order.getStatus());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setPromotionCode(order.getPromotionCode());
        entity.setDiscountAmount(order.getDiscountAmount());
        entity.setCreatedAt(order.getCreatedAt());
        order.getItems().forEach(item -> {
            OrderItemJpaEntity itemEntity = new OrderItemJpaEntity();
            itemEntity.setId(item.getId());
            itemEntity.setTicketTypeId(item.getTicketTypeId());
            itemEntity.setTicketTypeName(item.getTicketTypeName());
            itemEntity.setSeatId(item.getSeatId());
            itemEntity.setSeatRow(item.getSeatRow());
            itemEntity.setSeatNumber(item.getSeatNumber());
            itemEntity.setUnitPrice(item.getUnitPrice());
            itemEntity.setOrderId(order.getId());
            entity.getItems().add(itemEntity);
        });
        return entity;
    }

    private Order toDomain(OrderJpaEntity entity) {
        Order order = new Order(entity.getId(), entity.getOrderNumber(), entity.getUserId(),
                entity.getStatus(), entity.getTotalAmount(), entity.getPromotionCode(),
                entity.getDiscountAmount(), entity.getCreatedAt());
        if (entity.getItems() != null) {
            for (OrderItemJpaEntity itemEntity : entity.getItems()) {
                order.addItem(new OrderItem(itemEntity.getId(), itemEntity.getTicketTypeId(),
                        itemEntity.getTicketTypeName(), itemEntity.getUnitPrice(), itemEntity.getSeatId(),
                        itemEntity.getSeatRow(), itemEntity.getSeatNumber()));
            }
        }
        return order;
    }
}