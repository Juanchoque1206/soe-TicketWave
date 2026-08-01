package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.domain.event.model.Event;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.domain.event.repository.EventRepository;
import com.ticketwave.infrastructure.repository.jpa.JpaVenue;
import com.ticketwave.infrastructure.repository.jpa.JpaVenueRepository;
import com.ticketwave.infrastructure.repository.jpa.JpaSection;
import com.ticketwave.infrastructure.repository.jpa.JpaSectionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class EventPersistenceAdapter implements EventRepository {

    private final JpaEventRepository jpaEventRepository;
    private final JpaVenueRepository jpaVenueRepository;
    private final JpaSectionRepository jpaSectionRepository;
    private final EventPersistenceMapper mapper;

    public EventPersistenceAdapter(JpaEventRepository jpaEventRepository,
                                    JpaVenueRepository jpaVenueRepository,
                                    JpaSectionRepository jpaSectionRepository,
                                    EventPersistenceMapper mapper) {
        this.jpaEventRepository = jpaEventRepository;
        this.jpaVenueRepository = jpaVenueRepository;
        this.jpaSectionRepository = jpaSectionRepository;
        this.mapper = mapper;
    }

    @Override
    public Event save(Event event) {
        JpaEvent jpaEvent;
        if (event.getId() != null) {
            jpaEvent = jpaEventRepository.findById(event.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Event", "id", event.getId()));
            jpaEvent.setName(event.getName());
            jpaEvent.setDescription(event.getDescription());
            jpaEvent.setCategory(event.getCategory());
            jpaEvent.setStatus(event.getStatus());
            jpaEvent.setArtist(event.getArtist());
            jpaEvent.setEventDate(event.getEventDate());
            jpaEvent.setSalesStartDate(event.getSalesStartDate());
            jpaEvent.setSalesEndDate(event.getSalesEndDate());
        } else {
            jpaEvent = new JpaEvent();
            jpaEvent.setName(event.getName());
            jpaEvent.setDescription(event.getDescription());
            jpaEvent.setCategory(event.getCategory());
            jpaEvent.setStatus(event.getStatus());
            jpaEvent.setArtist(event.getArtist());
            jpaEvent.setEventDate(event.getEventDate());
            jpaEvent.setSalesStartDate(event.getSalesStartDate());
            jpaEvent.setSalesEndDate(event.getSalesEndDate());

            JpaVenue venue = jpaVenueRepository.findById(event.getVenueId())
                    .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", event.getVenueId()));
            jpaEvent.setVenue(venue);

            for (com.ticketwave.domain.event.model.TicketType tt : event.getTicketTypes()) {
                JpaTicketType jpaTt = new JpaTicketType();
                jpaTt.setName(tt.getName());
                jpaTt.setPrice(tt.getPrice());
                jpaTt.setTotalQuantity(tt.getTotalQuantity());
                jpaTt.setEvent(jpaEvent);
                if (tt.getSectionId() != null) {
                    JpaSection section = jpaSectionRepository.findById(tt.getSectionId())
                            .orElseThrow(() -> new ResourceNotFoundException("Section", "id", tt.getSectionId()));
                    jpaTt.setSection(section);
                }
                jpaEvent.getTicketTypes().add(jpaTt);
            }
        }

        jpaEvent = jpaEventRepository.save(jpaEvent);
        return mapper.toDomain(jpaEvent);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return jpaEventRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Page<Event> searchEvents(EventStatus status, String city, String artist,
                                     Long venueId, LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable) {
        return jpaEventRepository.searchEvents(status, city, artist, venueId, fromDate, toDate, pageable)
                .map(mapper::toDomain);
    }
}
