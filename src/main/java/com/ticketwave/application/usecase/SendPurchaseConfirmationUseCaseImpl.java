package com.ticketwave.application.usecase;

import com.ticketwave.application.usecase.SendPurchaseConfirmationUseCase;
import com.ticketwave.domain.notification.repository.NotificationSender;
import org.springframework.stereotype.Service;

@Service
public class SendPurchaseConfirmationUseCaseImpl implements SendPurchaseConfirmationUseCase {

    private final NotificationSender notificationSender;

    public SendPurchaseConfirmationUseCaseImpl(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @Override
    public void sendPurchaseConfirmation(Long userId, Long orderId) {
        notificationSender.sendPurchaseConfirmation(userId, orderId);
    }
}
