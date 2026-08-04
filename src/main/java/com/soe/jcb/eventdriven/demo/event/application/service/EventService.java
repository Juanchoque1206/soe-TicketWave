package com.soe.jcb.eventdriven.demo.event.application.service;

import com.soe.jcb.eventdriven.demo.common.domain.PageResult;
import com.soe.jcb.eventdriven.demo.common.domain.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.common.domain.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.event.application.in.EventUseCase;
import com.soe.jcb.eventdriven.demo.event.application.out.VenueExistsChecker;
import com.soe.jcb.eventdriven.demo.event.domain.Event;
import com.soe.jcb.eventdriven.demo.event.domain.EventRepository;
import com.soe.jcb.eventdriven.demo.event.domain.TicketType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventService implements EventUseCase {

    private final EventRepository eventRepository;
    private final VenueExistsChecker venueExistsChecker;

    public EventService(EventRepository eventRepository, VenueExistsChecker venueExistsChecker) {
        this.eventRepository = eventRepository;
        this.venueExistsChecker = venueExistsChecker;
    }

    @Override
    @Transactional
    public EventResult create(CreateEventCommand command) {
        if (command.venueId() != null && !venueExistsChecker.existsById(command.venueId())) {
            throw new BusinessRuleException("Venue with id " + command.venueId() + " does not exist");
        }
        Event event = new Event(null, command.name(), command.description(), command.category(),
                command.artist(), command.eventDate(), command.salesStartDate(), command.salesEndDate(),
                command.venueId());
        if (command.ticketTypes() != null) {
            command.ticketTypes().forEach(tt -> event.addTicketType(
                    new TicketType(null, tt.name(), tt.price(), tt.totalQuantity(), 0, tt.sectionId())));
        }
        return toResult(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventResult update(Long id, UpdateEventCommand command) {
        Event event = getEvent(id);
        event.setStatus(command.status());
        return toResult(eventRepository.save(event));
    }

    @Override
    @Transactional(readOnly = true)
    public EventResult findById(Long id) {
        return toResult(getEvent(id));
    }

    @Override
    @Transactional(readOnly = true)
    public PageResult<EventResult> search(EventQuery query) {
        PageResult<Event> page = eventRepository.search(Event.Status.PUBLISHED, query.city(),
                query.artist(), query.venueId(), query.from(), query.to(), query.page(), query.size());
        List<EventResult> content = page.content().stream()
                .map(e -> new EventResult(e.getId(), e.getName(), e.getDescription(), e.getCategory(),
                        e.getStatus(), e.getArtist(), e.getEventDate(), e.getSalesStartDate(),
                        e.getSalesEndDate(), e.getVenueId(), toTicketResults(e.getTicketTypes())))
                .toList();
        return new PageResult<>(content, page.page(), page.size(), page.totalElements(), page.totalPages());
    }

    @Override
    @Transactional
    public EventResult cancel(Long id) {
        Event event = getEvent(id);
        if (event.getStatus() == Event.Status.CANCELLED) {
            throw new BusinessRuleException("Event is already cancelled");
        }
        event.setStatus(Event.Status.CANCELLED);
        return toResult(eventRepository.save(event));
    }

    @Override
    @Transactional
    public EventResult postpone(Long id, LocalDateTime newDate) {
        Event event = getEvent(id);
        if (event.getStatus() == Event.Status.CANCELLED) {
            throw new BusinessRuleException("Cannot postpone a cancelled event");
        }
        event.setStatus(Event.Status.POSTPONED);
        return toResult(eventRepository.save(event));
    }

    private Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
    }

    private List<TicketTypeResult> toTicketResults(List<TicketType> ticketTypes) {
        return ticketTypes.stream()
                .map(tt -> new TicketTypeResult(tt.getId(), tt.getName(), tt.getPrice(),
                        tt.getTotalQuantity(), tt.getSoldQuantity(), tt.getSectionId()))
                .toList();
    }

    private EventResult toResult(Event event) {
        return new EventResult(event.getId(), event.getName(), event.getDescription(),
                event.getCategory(), event.getStatus(), event.getArtist(), event.getEventDate(),
                event.getSalesStartDate(), event.getSalesEndDate(), event.getVenueId(),
                toTicketResults(event.getTicketTypes()));
    }
}