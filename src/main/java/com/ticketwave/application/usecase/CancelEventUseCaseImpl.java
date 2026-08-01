package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.application.mapper.EventMapper;
import com.ticketwave.domain.event.model.Event;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.application.usecase.CancelEventUseCase;
import com.ticketwave.domain.event.repository.EventRepository;
import com.ticketwave.domain.event.service.EventStatusMachine;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CancelEventUseCaseImpl implements CancelEventUseCase {

    private final EventRepository eventRepository;
    private final EventStatusMachine eventStatusMachine;
    private final EventMapper eventMapper;

    public CancelEventUseCaseImpl(EventRepository eventRepository,
                                   EventStatusMachine eventStatusMachine,
                                   EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventStatusMachine = eventStatusMachine;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional
    @CacheEvict(value = "events", key = "#id")
    public EventResponse cancel(Long id) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        eventStatusMachine.validateTransition(event.getStatus(), EventStatus.CANCELLED);
        event.setStatus(EventStatus.CANCELLED);

        event = eventRepository.save(event);
        return eventMapper.toResponse(event);
    }
}
