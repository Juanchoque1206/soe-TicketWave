package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.EventCreateRequest;
import com.ticketwave.application.dto.EventResponse;

public interface CreateEventUseCase {
    EventResponse create(EventCreateRequest request);
}
