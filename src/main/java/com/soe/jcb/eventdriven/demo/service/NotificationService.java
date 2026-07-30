package com.soe.jcb.eventdriven.demo.service;

public interface NotificationService {

    void sendPurchaseConfirmation(Long userId, Long orderId);

    void sendEventUpdate(Long eventId, String message);

    void sendCancellationNotice(Long userId, Long orderId);
}
