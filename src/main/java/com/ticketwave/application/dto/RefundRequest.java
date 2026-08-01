package com.ticketwave.application.dto;

import java.io.Serializable;

public record RefundRequest(
        String reason
) implements Serializable {
}
