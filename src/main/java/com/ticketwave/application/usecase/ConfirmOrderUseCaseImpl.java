package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.application.mapper.OrderMapper;
import com.ticketwave.domain.ticket.event.OrderConfirmedEvent;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.application.usecase.ConfirmOrderUseCase;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ConfirmOrderUseCaseImpl implements ConfirmOrderUseCase {

    private final OrderRepository orderRepository;
    private final DomainEventBus eventBus;
    private final OrderMapper orderMapper;

    public ConfirmOrderUseCaseImpl(OrderRepository orderRepository,
                                   DomainEventBus eventBus,
                                   OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse confirm(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Order cannot be confirmed in status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order = orderRepository.save(order);

        eventBus.publish(new OrderConfirmedEvent(
                order.getId(), order.getOrderNumber(), order.getUserId()));

        return orderMapper.toResponse(order);
    }
}
