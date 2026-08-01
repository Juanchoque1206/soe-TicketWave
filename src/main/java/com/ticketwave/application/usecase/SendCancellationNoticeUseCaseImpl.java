package com.ticketwave.application.usecase;

import com.ticketwave.application.usecase.SendCancellationNoticeUseCase;
import com.ticketwave.domain.notification.repository.NotificationSender;
import org.springframework.stereotype.Service;

@Service
public class SendCancellationNoticeUseCaseImpl implements SendCancellationNoticeUseCase {

    private final NotificationSender notificationSender;

    public SendCancellationNoticeUseCaseImpl(NotificationSender notificationSender) {
        this.notificationSender = notificationSender;
    }

    @Override
    public void sendCancellationNotice(Long userId, Long orderId) {
        notificationSender.sendCancellationNotice(userId, orderId);
    }
}
