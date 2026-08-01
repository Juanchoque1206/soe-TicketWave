package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.application.mapper.EventMapper;
import com.ticketwave.domain.event.model.Event;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.application.usecase.PostponeEventUseCase;
import com.ticketwave.domain.event.repository.EventRepository;
import com.ticketwave.domain.event.service.EventStatusMachine;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PostponeEventUseCaseImpl implements PostponeEventUseCase {

    private final EventRepository eventRepository;
    private final EventStatusMachine eventStatusMachine;
    private final EventMapper eventMapper;

    public PostponeEventUseCaseImpl(EventRepository eventRepository,
                                     EventStatusMachine eventStatusMachine,
                                     EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventStatusMachine = eventStatusMachine;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional
    @CacheEvict(value = "events", key = "#id")
    public EventResponse postpone(Long id, LocalDateTime newDate) {
        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));

        eventStatusMachine.validateTransition(event.getStatus(), EventStatus.POSTPONED);
        event.setStatus(EventStatus.POSTPONED);
        event.setEventDate(newDate);

        event = eventRepository.save(event);
        return eventMapper.toResponse(event);
    }
}
