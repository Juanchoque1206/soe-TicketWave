package com.ticketwave.application.usecase;

public interface SendPurchaseConfirmationUseCase {
    void sendPurchaseConfirmation(Long userId, Long orderId);
}
