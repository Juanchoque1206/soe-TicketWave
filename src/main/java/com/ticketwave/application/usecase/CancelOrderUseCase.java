package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.OrderResponse;

public interface CancelOrderUseCase {

    OrderResponse cancelOrder(String orderNumber, Long userId);
}
