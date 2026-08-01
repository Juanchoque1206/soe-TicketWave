package com.ticketwave.domain.event.repository;

import com.ticketwave.domain.event.model.Event;
import com.ticketwave.domain.event.model.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Optional;

public interface EventRepository {
    Event save(Event event);
    Optional<Event> findById(Long id);
    Page<Event> searchEvents(EventStatus status, String city, String artist,
                              Long venueId, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
}
