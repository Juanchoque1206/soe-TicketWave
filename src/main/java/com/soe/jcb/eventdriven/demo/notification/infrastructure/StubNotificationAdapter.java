package com.soe.jcb.eventdriven.demo.notification.infrastructure;

import com.soe.jcb.eventdriven.demo.notification.domain.NotificationSender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Stub notification adapter for local development; logs instead of sending.
 * Swap this bean for a real provider adapter in production.
 */
@Component
public class StubNotificationAdapter implements NotificationSender {

    private static final Logger log = LoggerFactory.getLogger(StubNotificationAdapter.class);

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