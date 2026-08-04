package com.soe.jcb.eventdriven.demo.notification.domain;

/**
 * Domain port for sending notifications to users. Implemented by adapters
 * (log/stub locally, email/push/SMS providers in production).
 */
public interface NotificationSender {

    void sendPurchaseConfirmation(Long userId, Long orderId);

    void sendEventUpdate(Long eventId, String message);

    void sendCancellationNotice(Long userId, Long orderId);
}