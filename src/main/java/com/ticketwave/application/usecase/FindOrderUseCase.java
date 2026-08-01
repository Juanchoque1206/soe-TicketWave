package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.OrderResponse;

import java.util.List;

public interface FindOrderUseCase {

    List<OrderResponse> findByUser(Long userId);

    OrderResponse findByOrderNumber(String orderNumber, Long userId);
}
