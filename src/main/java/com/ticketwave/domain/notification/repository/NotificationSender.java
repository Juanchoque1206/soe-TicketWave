package com.ticketwave.domain.notification.repository;

public interface NotificationSender {
    void sendPurchaseConfirmation(Long userId, Long orderId);
    void sendEventUpdate(Long eventId, String message);
    void sendCancellationNotice(Long userId, Long orderId);
}
