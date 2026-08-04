package com.soe.jcb.eventdriven.demo.order.application.out;

/**
 * Messaging port used by the order context to publish domain events to the
 * saga/broker layer (RabbitMQ in production, in-memory in tests).
 */
public interface OrderMessagingPort {

    void publishOrderCreated(Long orderId, String orderNumber, Long userId);

    void publishOrderConfirmed(Long orderId, String orderNumber, Long userId);

    void publishOrderCancelled(Long orderId, String orderNumber, Long userId);
}