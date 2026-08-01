package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.OrderCreateRequest;
import com.ticketwave.application.dto.OrderResponse;

public interface BuyTicketUseCase {

    OrderResponse buy(Long userId, OrderCreateRequest request);
}
