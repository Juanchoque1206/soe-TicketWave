package com.soe.jcb.eventdriven.demo.common.saga;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderSagaRepository extends JpaRepository<OrderSaga, Long> {

    Optional<OrderSaga> findByOrderId(Long orderId);

    Optional<OrderSaga> findByOrderNumber(String orderNumber);
}
