package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.domain.event.repository.TicketTypeRepository;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.application.mapper.OrderMapper;
import com.ticketwave.domain.ticket.event.OrderCancelledEvent;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderItem;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.application.usecase.CancelOrderUseCase;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelOrderUseCaseImpl implements CancelOrderUseCase {

    private final OrderRepository orderRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final DomainEventBus eventBus;
    private final OrderMapper orderMapper;

    public CancelOrderUseCaseImpl(OrderRepository orderRepository,
                                  TicketTypeRepository ticketTypeRepository,
                                  DomainEventBus eventBus,
                                  OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.eventBus = eventBus;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (!order.getUserId().equals(userId)) {
            throw new BusinessRuleException("Order does not belong to the current user");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessRuleException("Order cannot be cancelled in status: " + order.getStatus());
        }

        for (OrderItem item : order.getItems()) {
            ticketTypeRepository.decrementSoldQuantity(item.getTicketTypeId(), 1);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        eventBus.publish(new OrderCancelledEvent(
                order.getId(), order.getOrderNumber(), userId));

        return orderMapper.toResponse(order);
    }
}
