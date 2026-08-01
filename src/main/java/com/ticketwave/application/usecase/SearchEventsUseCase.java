package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.PagedResponse;
import com.ticketwave.application.dto.EventSummaryResponse;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface SearchEventsUseCase {
    PagedResponse<EventSummaryResponse> search(String city, String artist, Long venueId,
                                                 LocalDateTime from, LocalDateTime to, Pageable pageable);
}
