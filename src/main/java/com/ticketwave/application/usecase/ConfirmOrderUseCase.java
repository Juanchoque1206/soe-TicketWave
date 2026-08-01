package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.OrderResponse;

public interface ConfirmOrderUseCase {

    OrderResponse confirm(Long orderId);
}
