package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderItem;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class OrderPersistenceAdapter implements OrderRepository {

    private final JpaOrderRepository jpaOrderRepository;
    private final TicketPersistenceMapper mapper;

    public OrderPersistenceAdapter(JpaOrderRepository jpaOrderRepository, TicketPersistenceMapper mapper) {
        this.jpaOrderRepository = jpaOrderRepository;
        this.mapper = mapper;
    }

    @Override
    public Order save(Order order) {
        JpaOrder jpa;
        if (order.getId() != null) {
            jpa = jpaOrderRepository.findById(order.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", order.getId()));
            jpa.setOrderNumber(order.getOrderNumber());
            jpa.setUserId(order.getUserId());
            jpa.setStatus(order.getStatus());
            jpa.setEventId(order.getEventId());
            jpa.setEventName(order.getEventName());
            jpa.setEventDate(order.getEventDate());
            jpa.setVenueName(order.getVenueName());
            jpa.setTotalAmount(order.getTotalAmount());
            jpa.setPromotionCode(order.getPromotionCode());
            jpa.setDiscountAmount(order.getDiscountAmount());
        } else {
            jpa = new JpaOrder();
            jpa.setOrderNumber(order.getOrderNumber());
            jpa.setUserId(order.getUserId());
            jpa.setStatus(order.getStatus());
            jpa.setEventId(order.getEventId());
            jpa.setEventName(order.getEventName());
            jpa.setEventDate(order.getEventDate());
            jpa.setVenueName(order.getVenueName());
            jpa.setTotalAmount(order.getTotalAmount());
            jpa.setPromotionCode(order.getPromotionCode());
            jpa.setDiscountAmount(order.getDiscountAmount());

            for (OrderItem item : order.getItems()) {
                JpaOrderItem jpaItem = new JpaOrderItem();
                jpaItem.setTicketTypeId(item.getTicketTypeId());
                jpaItem.setTicketTypeName(item.getTicketTypeName());
                jpaItem.setUnitPrice(item.getUnitPrice());
                jpaItem.setSeatId(item.getSeatId());
                jpaItem.setSeatRow(item.getSeatRow());
                jpaItem.setSeatNumber(item.getSeatNumber());
                jpa.addItem(jpaItem);
            }
        }

        jpa = jpaOrderRepository.save(jpa);
        return mapper.toDomain(jpa);
    }

    @Override
    public Optional<Order> findById(Long id) {
        return jpaOrderRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<Order> findByOrderNumber(String orderNumber) {
        return jpaOrderRepository.findByOrderNumber(orderNumber).map(mapper::toDomain);
    }

    @Override
    public List<Order> findByUserId(Long userId) {
        return jpaOrderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countByUserIdAndStatusAndCreatedAtAfter(Long userId, OrderStatus status, LocalDateTime after) {
        return jpaOrderRepository.countByUserIdAndStatusAndCreatedAtAfter(userId, status, after);
    }
}
