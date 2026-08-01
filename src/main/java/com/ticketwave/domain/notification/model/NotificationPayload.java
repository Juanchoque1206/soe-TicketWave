package com.ticketwave.domain.notification.model;

public record NotificationPayload(
        Long userId,
        String type,
        String subject,
        String body
) {}
