package com.ticketwave.infrastructure.adapter.notification;

import com.ticketwave.domain.notification.repository.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class StubNotificationSender implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(StubNotificationSender.class);

    @Override
    public void sendPurchaseConfirmation(Long userId, Long orderId) {
        log.info("[NOTIFICATION] Purchase confirmation sent to user {} for order {}", userId, orderId);
    }

    @Override
    public void sendEventUpdate(Long eventId, String message) {
        log.info("[NOTIFICATION] Event update for event {}: {}", eventId, message);
    }

    @Override
    public void sendCancellationNotice(Long userId, Long orderId) {
        log.info("[NOTIFICATION] Cancellation notice sent to user {} for order {}", userId, orderId);
    }
}
