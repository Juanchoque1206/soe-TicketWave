package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.payment.model.OrderInfo;
import com.ticketwave.domain.payment.repository.OrderQueryPort;
import com.ticketwave.infrastructure.repository.jpa.JpaOrderRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OrderQueryAdapter implements OrderQueryPort {

    private final JpaOrderRepository jpaOrderRepository;

    public OrderQueryAdapter(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public Optional<OrderInfo> findByOrderNumber(String orderNumber) {
        return jpaOrderRepository.findByOrderNumber(orderNumber)
                .map(order -> new OrderInfo(
                        order.getId(),
                        order.getOrderNumber(),
                        order.getUserId(),
                        order.getTotalAmount(),
                        order.getStatus().name()));
    }
}
