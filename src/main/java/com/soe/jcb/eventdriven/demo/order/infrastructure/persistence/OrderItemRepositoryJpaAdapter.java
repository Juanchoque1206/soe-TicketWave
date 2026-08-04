package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.order.domain.OrderItemRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderItemRepositoryJpaAdapter implements OrderItemRepository {

    private final OrderItemJpaRepository jpaRepository;

    public OrderItemRepositoryJpaAdapter(OrderItemJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public boolean isSeatReserved(Long eventId, Long seatId) {
        return jpaRepository.isSeatReserved(eventId, seatId);
    }
}