package com.soe.jcb.eventdriven.demo.event.application.in;

import com.soe.jcb.eventdriven.demo.common.domain.PageResult;
import com.soe.jcb.eventdriven.demo.event.domain.Event;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Use-case boundary for event management (pure, no Spring).
 */
public interface EventUseCase {

    EventResult create(CreateEventCommand command);

    EventResult update(Long id, UpdateEventCommand command);

    EventResult findById(Long id);

    PageResult<EventResult> search(EventQuery query);

    EventResult cancel(Long id);

    EventResult postpone(Long id, LocalDateTime newDate);

    record CreateEventCommand(String name, String description, Event.Category category,
                              String artist, LocalDateTime eventDate, LocalDateTime salesStartDate,
                              LocalDateTime salesEndDate, Long venueId, List<TicketTypeCommand> ticketTypes) {
    }

    record UpdateEventCommand(String name, String description, Event.Category category,
                              String artist, LocalDateTime eventDate, LocalDateTime salesStartDate,
                              LocalDateTime salesEndDate, Event.Status status) {
    }

    record TicketTypeCommand(String name, BigDecimal price, int totalQuantity, Long sectionId) {
    }

    record EventQuery(String city, String artist, Long venueId, LocalDateTime from, LocalDateTime to,
                      int page, int size) {
    }

    record TicketTypeResult(Long id, String name, BigDecimal price, int totalQuantity,
                            int soldQuantity, Long sectionId) {
    }

    record EventResult(Long id, String name, String description, Event.Category category,
                       Event.Status status, String artist, LocalDateTime eventDate,
                       LocalDateTime salesStartDate, LocalDateTime salesEndDate,
                       Long venueId, List<TicketTypeResult> ticketTypes) {
    }
}