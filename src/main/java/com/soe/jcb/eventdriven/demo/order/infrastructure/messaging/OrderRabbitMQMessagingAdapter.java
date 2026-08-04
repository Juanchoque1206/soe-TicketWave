package com.soe.jcb.eventdriven.demo.order.infrastructure.messaging;

import com.soe.jcb.eventdriven.demo.order.application.out.OrderMessagingPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Queue;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ implementation of {@link OrderMessagingPort}. Publishes order
 * domain events to the configured queue for downstream saga/payment/ticket
 * processing.
 */
@Component
public class OrderRabbitMQMessagingAdapter implements OrderMessagingPort {

    private static final Logger log = LoggerFactory.getLogger(OrderRabbitMQMessagingAdapter.class);

    private final AmqpTemplate amqpTemplate;
    private final Queue orderEventsQueue;

    public OrderRabbitMQMessagingAdapter(AmqpTemplate amqpTemplate, Queue orderEventsQueue) {
        this.amqpTemplate = amqpTemplate;
        this.orderEventsQueue = orderEventsQueue;
    }

    @Override
    public void publishOrderCreated(Long orderId, String orderNumber, Long userId) {
        publish("ORDER_CREATED", orderId, orderNumber, userId);
    }

    @Override
    public void publishOrderConfirmed(Long orderId, String orderNumber, Long userId) {
        publish("ORDER_CONFIRMED", orderId, orderNumber, userId);
    }

    @Override
    public void publishOrderCancelled(Long orderId, String orderNumber, Long userId) {
        publish("ORDER_CANCELLED", orderId, orderNumber, userId);
    }

    private void publish(String type, Long orderId, String orderNumber, Long userId) {
        OrderEvent event = new OrderEvent(type, orderId, orderNumber, userId);
        amqpTemplate.convertAndSend(orderEventsQueue.getName(), event);
        log.info("Published {} for order {} (user {})", type, orderNumber, userId);
    }

    public record OrderEvent(String type, Long orderId, String orderNumber, Long userId) {
    }
}