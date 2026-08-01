package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.EventCreateRequest;
import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.application.mapper.EventMapper;
import com.ticketwave.domain.event.model.Event;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.domain.event.model.TicketType;
import com.ticketwave.application.usecase.CreateEventUseCase;
import com.ticketwave.domain.event.repository.EventRepository;
import com.ticketwave.domain.event.repository.VenueQueryPort;
import com.ticketwave.application.dto.VenueResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateEventUseCaseImpl implements CreateEventUseCase {

    private final EventRepository eventRepository;
    private final VenueQueryPort venueQueryPort;
    private final EventMapper eventMapper;

    public CreateEventUseCaseImpl(EventRepository eventRepository,
                                   VenueQueryPort venueQueryPort,
                                   EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.venueQueryPort = venueQueryPort;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional
    public EventResponse create(EventCreateRequest request) {
        VenueResponse venue = venueQueryPort.findVenueById(request.venueId());

        Event event = new Event();
        event.setName(request.name());
        event.setDescription(request.description());
        event.setCategory(request.category());
        event.setArtist(request.artist());
        event.setEventDate(request.eventDate());
        event.setSalesStartDate(request.salesStartDate());
        event.setSalesEndDate(request.salesEndDate());
        event.setVenueId(venue.id());
        event.setVenueName(venue.name());
        event.setVenueCity(venue.city());
        event.setStatus(EventStatus.DRAFT);

        for (EventCreateRequest.TicketTypeRequest ttReq : request.ticketTypes()) {
            TicketType ticketType = new TicketType();
            ticketType.setName(ttReq.name());
            ticketType.setPrice(ttReq.price());
            ticketType.setTotalQuantity(ttReq.totalQuantity());
            ticketType.setSectionId(ttReq.sectionId());
            event.getTicketTypes().add(ticketType);
        }

        event = eventRepository.save(event);
        return eventMapper.toResponse(event);
    }
}
