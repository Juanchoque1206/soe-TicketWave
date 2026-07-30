package com.soe.jcb.eventdriven.demo.notification.dto;

public record NotificationPayload(
        Long userId,
        String type,
        String subject,
        String body
) {
}
