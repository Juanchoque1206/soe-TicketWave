package com.soe.jcb.eventdriven.demo.event.domain;

import com.soe.jcb.eventdriven.demo.common.domain.PageResult;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Domain port for persisting and searching {@link Event}.
 */
public interface EventRepository {

    Event save(Event event);

    Optional<Event> findById(Long id);

    /**
     * Searches published events using framework-independent paging results.
     */
    PageResult<Event> search(Event.Status status, String city, String artist, Long venueId,
                             LocalDateTime from, LocalDateTime to, int page, int size);
}