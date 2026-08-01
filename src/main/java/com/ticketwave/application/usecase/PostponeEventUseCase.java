package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.EventResponse;
import java.time.LocalDateTime;

public interface PostponeEventUseCase {
    EventResponse postpone(Long id, LocalDateTime newDate);
}
