package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.EventResponse;

public interface CancelEventUseCase {
    EventResponse cancel(Long id);
}
