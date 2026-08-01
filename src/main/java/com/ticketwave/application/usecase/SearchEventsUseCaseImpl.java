package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.PagedResponse;
import com.ticketwave.application.dto.EventSummaryResponse;
import com.ticketwave.application.mapper.EventMapper;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.application.usecase.SearchEventsUseCase;
import com.ticketwave.domain.event.repository.EventRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class SearchEventsUseCaseImpl implements SearchEventsUseCase {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public SearchEventsUseCaseImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "eventSearch", key = "#city + '_' + #artist + '_' + #venueId + '_' + #from + '_' + #to + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public PagedResponse<EventSummaryResponse> search(String city, String artist, Long venueId,
                                                        LocalDateTime from, LocalDateTime to, Pageable pageable) {
        var page = eventRepository.searchEvents(EventStatus.PUBLISHED, city, artist, venueId, from, to, pageable);
        return PagedResponse.from(page, eventMapper::toSummaryResponse);
    }
}
