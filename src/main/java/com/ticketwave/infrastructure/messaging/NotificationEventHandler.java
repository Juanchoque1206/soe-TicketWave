package com.ticketwave.infrastructure.messaging;

import com.ticketwave.application.usecase.SendCancellationNoticeUseCase;
import com.ticketwave.application.usecase.SendPurchaseConfirmationUseCase;
import com.ticketwave.domain.ticket.event.OrderCancelledEvent;
import com.ticketwave.domain.ticket.event.OrderConfirmedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class NotificationEventHandler {

    private static final Logger log = LoggerFactory.getLogger(NotificationEventHandler.class);

    private final SendPurchaseConfirmationUseCase sendPurchaseConfirmationUseCase;
    private final SendCancellationNoticeUseCase sendCancellationNoticeUseCase;

    public NotificationEventHandler(SendPurchaseConfirmationUseCase sendPurchaseConfirmationUseCase,
                                     SendCancellationNoticeUseCase sendCancellationNoticeUseCase) {
        this.sendPurchaseConfirmationUseCase = sendPurchaseConfirmationUseCase;
        this.sendCancellationNoticeUseCase = sendCancellationNoticeUseCase;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Sending purchase confirmation for order {}", event.getOrderNumber());
        sendPurchaseConfirmationUseCase.sendPurchaseConfirmation(event.getUserId(), event.getOrderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onOrderCancelled(OrderCancelledEvent event) {
        log.info("Sending cancellation notice for order {}", event.getOrderNumber());
        sendCancellationNoticeUseCase.sendCancellationNotice(event.getUserId(), event.getOrderId());
    }
}
