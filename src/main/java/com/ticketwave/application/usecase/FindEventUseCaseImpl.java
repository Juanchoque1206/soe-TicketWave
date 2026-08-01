package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.application.mapper.EventMapper;
import com.ticketwave.application.usecase.FindEventUseCase;
import com.ticketwave.domain.event.repository.EventRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindEventUseCaseImpl implements FindEventUseCase {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public FindEventUseCaseImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "events", key = "#id")
    public EventResponse findById(Long id) {
        return eventRepository.findById(id)
                .map(eventMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", id));
    }
}
