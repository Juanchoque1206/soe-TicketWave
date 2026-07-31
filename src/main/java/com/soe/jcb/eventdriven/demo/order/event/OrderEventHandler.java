package com.soe.jcb.eventdriven.demo.order.event;

import com.soe.jcb.eventdriven.demo.common.event.DomainEventBus;
import com.soe.jcb.eventdriven.demo.common.saga.SagaCoordinator;
import com.soe.jcb.eventdriven.demo.common.saga.SagaFailedEvent;
import com.soe.jcb.eventdriven.demo.common.saga.SagaStatus;
import com.soe.jcb.eventdriven.demo.order.service.OrderService;
import com.soe.jcb.eventdriven.demo.payment.event.PaymentCompletedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventHandler {

    private static final Logger log = LoggerFactory.getLogger(OrderEventHandler.class);

    private final OrderService orderService;
    private final DomainEventBus eventBus;
    private final SagaCoordinator sagaCoordinator;

    public OrderEventHandler(OrderService orderService, DomainEventBus eventBus, SagaCoordinator sagaCoordinator) {
        this.orderService = orderService;
        this.eventBus = eventBus;
        this.sagaCoordinator = sagaCoordinator;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Handling PaymentCompletedEvent for order {}", event.getOrderNumber());
        try {
            var response = orderService.confirmOrder(event.getOrderId());
            sagaCoordinator.advanceSaga(event.getOrderId(), SagaStatus.ORDER_CONFIRMED);
            eventBus.publish(new OrderConfirmedEvent(
                    response.id(), response.orderNumber(), event.getUserId()));
        } catch (Exception e) {
            log.error("Failed to confirm order {}: {}", event.getOrderNumber(), e.getMessage());
            sagaCoordinator.failSaga(event.getOrderId(), "Order confirmation failed: " + e.getMessage());
            eventBus.publish(new SagaFailedEvent(
                    event.getOrderId(), event.getOrderNumber(), event.getUserId(),
                    "Order confirmation failed: " + e.getMessage(), "ORDER_CONFIRMATION"));
        }
    }
}
