package com.soe.jcb.eventdriven.demo.service;

import com.soe.jcb.eventdriven.demo.dto.EventCreateRequest;
import com.soe.jcb.eventdriven.demo.dto.EventResponse;
import com.soe.jcb.eventdriven.demo.dto.EventSummaryResponse;
import com.soe.jcb.eventdriven.demo.dto.EventUpdateRequest;
import com.soe.jcb.eventdriven.demo.dto.PagedResponse;
import com.soe.jcb.eventdriven.demo.entity.Event;
import com.soe.jcb.eventdriven.demo.entity.EventStatus;
import com.soe.jcb.eventdriven.demo.entity.Section;
import com.soe.jcb.eventdriven.demo.entity.TicketType;
import com.soe.jcb.eventdriven.demo.entity.Venue;
import com.soe.jcb.eventdriven.demo.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.repository.EventRepository;
import com.soe.jcb.eventdriven.demo.repository.SectionRepository;
import com.soe.jcb.eventdriven.demo.repository.VenueRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EventService {

    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;
    private final SectionRepository sectionRepository;

    public EventService(EventRepository eventRepository,
                        VenueRepository venueRepository,
                        SectionRepository sectionRepository) {
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public EventResponse create(EventCreateRequest request) {
        Venue venue = venueRepository.findById(request.venueId())
                .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", request.venueId()));

        Event event = new Event();
        event.setName(request.name());
        event.setDescription(request.description());
        event.setCategory(request.category());
        event.setArtist(request.artist());
        event.setEventDate(request.eventDate());
        event.setSalesStartDate(request.salesStartDate());
        event.setSalesEndDate(request.salesEndDate());
        event.setVenue(venue);
        event.setStatus(EventStatus.DRAFT);

        for (EventCreateRequest.TicketTypeRequest ttReq : request.ticketTypes()) {
            TicketType ticketType = new TicketType();
            ticketType.setName(ttReq.name());
            ticketType.setPrice(ttReq.price());
            ticketType.setTotalQuantity(ttReq.totalQuantity());
            ticketType.setEvent(event);

            if (ttReq.sectionId() != null) {
                Section section = sectionRepository.findById(ttReq.sectionId())
                        .orElseThrow(() -> new ResourceNotFoundException("Section", "id", ttReq.sectionId()));
                ticketType.setSection(section);
            }

            event.getTicketTypes().add(ticketType);
        }

        event = eventRepository.save(event);
        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse update(Long id, EventUpdateRequest request) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        if (request.name() != null) event.setName(request.name());
        if (request.description() != null) event.setDescription(request.description());
        if (request.category() != null) event.setCategory(request.category());
        if (request.artist() != null) event.setArtist(request.artist());
        if (request.eventDate() != null) event.setEventDate(request.eventDate());
        if (request.salesStartDate() != null) event.setSalesStartDate(request.salesStartDate());
        if (request.salesEndDate() != null) event.setSalesEndDate(request.salesEndDate());
        if (request.status() != null) {
            validateStatusTransition(event.getStatus(), request.status());
            event.setStatus(request.status());
        }

        event = eventRepository.save(event);
        return EventResponse.from(event);
    }

    @Transactional(readOnly = true)
    public EventResponse findById(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
        return EventResponse.from(event);
    }

    @Transactional(readOnly = true)
    public PagedResponse<EventSummaryResponse> search(String city, String artist,
                                                       Long venueId, LocalDateTime from,
                                                       LocalDateTime to, Pageable pageable) {
        Page<Event> page = eventRepository.searchEvents(
                EventStatus.PUBLISHED, city, artist, venueId, from, to, pageable);
        return PagedResponse.from(page, EventSummaryResponse::from);
    }

    @Transactional
    public EventResponse cancel(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        validateStatusTransition(event.getStatus(), EventStatus.CANCELLED);
        event.setStatus(EventStatus.CANCELLED);

        event = eventRepository.save(event);
        return EventResponse.from(event);
    }

    @Transactional
    public EventResponse postpone(Long id, LocalDateTime newDate) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        validateStatusTransition(event.getStatus(), EventStatus.POSTPONED);
        event.setStatus(EventStatus.POSTPONED);
        event.setEventDate(newDate);

        event = eventRepository.save(event);
        return EventResponse.from(event);
    }

    private void validateStatusTransition(EventStatus current, EventStatus target) {
        boolean valid = switch (current) {
            case DRAFT -> target == EventStatus.PUBLISHED || target == EventStatus.CANCELLED;
            case PUBLISHED -> target == EventStatus.POSTPONED || target == EventStatus.CANCELLED || target == EventStatus.COMPLETED;
            case POSTPONED -> target == EventStatus.PUBLISHED || target == EventStatus.CANCELLED;
            case CANCELLED, COMPLETED -> false;
        };

        if (!valid) {
            throw new BusinessRuleException(
                    String.format("Invalid status transition from %s to %s", current, target));
        }
    }
}
