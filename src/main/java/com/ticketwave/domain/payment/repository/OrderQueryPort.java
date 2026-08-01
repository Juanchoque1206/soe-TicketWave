package com.ticketwave.domain.payment.repository;

import com.ticketwave.domain.payment.model.OrderInfo;

import java.util.Optional;

public interface OrderQueryPort {

    Optional<OrderInfo> findByOrderNumber(String orderNumber);
}
