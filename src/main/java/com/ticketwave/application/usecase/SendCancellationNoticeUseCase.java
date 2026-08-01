package com.ticketwave.application.usecase;

public interface SendCancellationNoticeUseCase {
    void sendCancellationNotice(Long userId, Long orderId);
}
