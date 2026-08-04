package com.soe.jcb.eventdriven.demo.saga.messaging;

import com.soe.jcb.eventdriven.demo.order.infrastructure.messaging.OrderRabbitMQMessagingAdapter.OrderEvent;
import com.soe.jcb.eventdriven.demo.saga.saga_orchestrator.SagaOrchestrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

/**
 * RabbitMQ consumer that drives the saga from published order events.
 * Idempotency is guaranteed by {@link com.soe.jcb.eventdriven.demo.saga.saga_orchestrator.SagaStateManager}.
 */
@Component
public class OrderEventListener {

    private static final Logger log = LoggerFactory.getLogger(OrderEventListener.class);

    private final SagaOrchestrator sagaOrchestrator;

    public OrderEventListener(SagaOrchestrator sagaOrchestrator) {
        this.sagaOrchestrator = sagaOrchestrator;
    }

    @RabbitListener(queues = "ticketwave.order.events")
    public void onOrderEvent(OrderEvent event) {
        log.info("Saga listener received {} for order {}", event.type(), event.orderNumber());
        switch (event.type()) {
            case "ORDER_CONFIRMED" -> sagaOrchestrator.handleOrderConfirmed(event.orderId());
            case "ORDER_CANCELLED" -> sagaOrchestrator.handleOrderCancelled(event.orderId());
            default -> log.debug("Ignoring order event type {}", event.type());
        }
    }
}