package com.soe.jcb.eventdriven.demo.dto;

public record NotificationPayload(
        Long userId,
        String type,
        String subject,
        String body
) {
}
