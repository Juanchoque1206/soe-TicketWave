package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.application.mapper.OrderMapper;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.application.usecase.FindOrderUseCase;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FindOrderUseCaseImpl implements FindOrderUseCase {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public FindOrderUseCaseImpl(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findByUser(Long userId) {
        return orderRepository.findByUserId(userId).stream()
                .map(orderMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findByOrderNumber(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (!order.getUserId().equals(userId)) {
            throw new BusinessRuleException("Order does not belong to the current user");
        }

        return orderMapper.toResponse(order);
    }
}
