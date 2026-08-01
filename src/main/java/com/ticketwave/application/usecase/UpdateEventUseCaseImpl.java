package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.application.dto.EventUpdateRequest;
import com.ticketwave.application.mapper.EventMapper;
import com.ticketwave.domain.event.model.Event;
import com.ticketwave.application.usecase.UpdateEventUseCase;
import com.ticketwave.domain.event.repository.EventRepository;
import com.ticketwave.domain.event.service.EventStatusMachine;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UpdateEventUseCaseImpl implements UpdateEventUseCase {

    private final EventRepository eventRepository;
    private final EventStatusMachine eventStatusMachine;
    private final EventMapper eventMapper;

    public UpdateEventUseCaseImpl(EventRepository eventRepository,
                                   EventStatusMachine eventStatusMachine,
                                   EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventStatusMachine = eventStatusMachine;
        this.eventMapper = eventMapper;
    }

    @Override
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
            eventStatusMachine.validateTransition(event.getStatus(), request.status());
            event.setStatus(request.status());
        }

        event = eventRepository.save(event);
        return eventMapper.toResponse(event);
    }
}
