package com.soe.jcb.eventdriven.demo.notification.event;

import com.soe.jcb.eventdriven.demo.notification.service.NotificationService;
import com.soe.jcb.eventdriven.demo.order.event.OrderConfirmedEvent;
import com.soe.jcb.eventdriven.demo.order.event.OrderCancelledEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventHandler.class);

    private final NotificationService notificationService;

    public NotificationEventHandler(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Sending purchase confirmation for order {}", event.getOrderNumber());
        notificationService.sendPurchaseConfirmation(event.getUserId(), event.getOrderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCancelled(OrderCancelledEvent event) {
        log.info("Sending cancellation notice for order {}", event.getOrderNumber());
        notificationService.sendCancellationNotice(event.getUserId(), event.getOrderId());
    }
}
