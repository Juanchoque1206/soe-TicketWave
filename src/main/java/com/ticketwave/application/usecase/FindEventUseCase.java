package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.EventResponse;

public interface FindEventUseCase {
    EventResponse findById(Long id);
}
