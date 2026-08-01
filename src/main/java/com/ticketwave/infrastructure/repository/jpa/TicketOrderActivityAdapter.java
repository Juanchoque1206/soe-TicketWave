package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.antifraud.repository.OrderActivityPort;
import com.ticketwave.domain.ticket.model.OrderStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TicketOrderActivityAdapter implements OrderActivityPort {

    private final JpaOrderRepository jpaOrderRepository;

    public TicketOrderActivityAdapter(JpaOrderRepository jpaOrderRepository) {
        this.jpaOrderRepository = jpaOrderRepository;
    }

    @Override
    public long countPendingOrdersSince(Long userId, LocalDateTime since) {
        return jpaOrderRepository.countByUserIdAndStatusAndCreatedAtAfter(userId, OrderStatus.PENDING, since);
    }
}
