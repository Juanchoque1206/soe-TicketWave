package com.soe.jcb.eventdriven.demo.event.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.common.domain.PageResult;
import com.soe.jcb.eventdriven.demo.event.domain.Event;
import com.soe.jcb.eventdriven.demo.event.domain.EventRepository;
import com.soe.jcb.eventdriven.demo.event.domain.TicketType;
import com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence.VenueJpaEntity;
import com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence.VenueJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class EventRepositoryJpaAdapter implements EventRepository {

    private final EventJpaRepository eventJpaRepository;
    private final TicketTypeJpaRepository ticketTypeJpaRepository;
    private final VenueJpaRepository venueJpaRepository;

    public EventRepositoryJpaAdapter(EventJpaRepository eventJpaRepository,
                                     TicketTypeJpaRepository ticketTypeJpaRepository,
                                     VenueJpaRepository venueJpaRepository) {
        this.eventJpaRepository = eventJpaRepository;
        this.ticketTypeJpaRepository = ticketTypeJpaRepository;
        this.venueJpaRepository = venueJpaRepository;
    }

    @Override
    @Transactional
    public Event save(Event event) {
        EventJpaEntity entity = toJpa(event);
        EventJpaEntity saved = eventJpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Event> findById(Long id) {
        return eventJpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public PageResult<Event> search(Event.Status status, String city, String artist, Long venueId,
                                    LocalDateTime from, LocalDateTime to, int page, int size) {
        List<Long> venueIds = null;
        if (city != null && !city.isBlank()) {
            venueIds = venueJpaRepository.findByCity(city).stream()
                    .map(VenueJpaEntity::getId)
                    .toList();
        }
        Page<EventJpaEntity> entityPage = eventJpaRepository.searchEvents(
                status, venueIds, artist, venueId, from, to, PageRequest.of(page, size));
        List<Event> events = entityPage.getContent().stream().map(this::toDomain).toList();
        return new PageResult<>(events, entityPage.getNumber(), entityPage.getSize(),
                entityPage.getTotalElements(), entityPage.getTotalPages());
    }

    private EventJpaEntity toJpa(Event event) {
        EventJpaEntity entity = new EventJpaEntity();
        setEntityFields(entity, event);
        entity.setVenueId(event.getVenueId());
        event.getTicketTypes().forEach(tt -> {
            TicketTypeJpaEntity ttEntity = new TicketTypeJpaEntity();
            ttEntity.setId(tt.getId());
            ttEntity.setName(tt.getName());
            ttEntity.setPrice(tt.getPrice());
            ttEntity.setTotalQuantity(tt.getTotalQuantity());
            ttEntity.setSoldQuantity(tt.getSoldQuantity());
            ttEntity.setSectionId(tt.getSectionId());
            ttEntity.setEventId(event.getId());
            entity.getTicketTypes().add(ttEntity);
        });
        return entity;
    }

    private void setEntityFields(EventJpaEntity entity, Event event) {
        entity.setName(event.getName());
        entity.setDescription(event.getDescription());
        entity.setCategory(event.getCategory());
        entity.setStatus(event.getStatus());
        entity.setArtist(event.getArtist());
        entity.setEventDate(event.getEventDate());
        entity.setSalesStartDate(event.getSalesStartDate());
        entity.setSalesEndDate(event.getSalesEndDate());
    }

    private Event toDomain(EventJpaEntity entity) {
        Event event = new Event(entity.getId(), entity.getName(), entity.getDescription(),
                entity.getCategory(), entity.getArtist(), entity.getEventDate(),
                entity.getSalesStartDate(), entity.getSalesEndDate(), entity.getVenueId());
        event.setId(entity.getId());
        event.setStatus(entity.getStatus());
        if (entity.getTicketTypes() != null) {
            for (TicketTypeJpaEntity ttEntity : entity.getTicketTypes()) {
                event.addTicketType(toTicketType(ttEntity));
            }
        }
        return event;
    }

    private TicketType toTicketType(TicketTypeJpaEntity entity) {
        return new TicketType(entity.getId(), entity.getName(), entity.getPrice(),
                entity.getTotalQuantity(), entity.getSoldQuantity(), entity.getSectionId());
    }
}