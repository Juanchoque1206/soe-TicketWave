package com.ticketwave.application.usecase;

import com.ticketwave.application.usecase.SendEventUpdateUseCase;
import com.ticketwave.domain.notification.repository.NotificationSender;
import org.springframework.stereotype.Service;

@Service
public class SendEventUpdateUseCaseImpl implements SendEventUpdateUseCase {

    private final NotificationSender notificationSender;

    public SendEventUpdateUseCaseImpl(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @Override
    public void sendEventUpdate(Long eventId, String message) {
        notificationSender.sendEventUpdate(eventId, message);
    }
}
