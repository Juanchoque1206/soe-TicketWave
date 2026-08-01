package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.EventUpdateRequest;
import com.ticketwave.application.dto.EventResponse;

public interface UpdateEventUseCase {
    EventResponse update(Long id, EventUpdateRequest request);
}
