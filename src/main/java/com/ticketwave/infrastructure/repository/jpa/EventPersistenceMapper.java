package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.event.model.Event;
import com.ticketwave.domain.event.model.TicketType;
import org.springframework.stereotype.Component;

@Component
public class EventPersistenceMapper {

    public Event toDomain(JpaEvent jpa) {
        Event event = new Event();
        event.setId(jpa.getId());
        event.setName(jpa.getName());
        event.setDescription(jpa.getDescription());
        event.setCategory(jpa.getCategory());
        event.setStatus(jpa.getStatus());
        event.setArtist(jpa.getArtist());
        event.setEventDate(jpa.getEventDate());
        event.setSalesStartDate(jpa.getSalesStartDate());
        event.setSalesEndDate(jpa.getSalesEndDate());
        event.setCreatedAt(jpa.getCreatedAt());
        event.setUpdatedAt(jpa.getUpdatedAt());
        if (jpa.getVenue() != null) {
            event.setVenueId(jpa.getVenue().getId());
            event.setVenueName(jpa.getVenue().getName());
            event.setVenueCity(jpa.getVenue().getCity());
        }
        if (jpa.getTicketTypes() != null) {
            event.setTicketTypes(jpa.getTicketTypes().stream().map(this::toDomain).toList());
        }
        return event;
    }

    public TicketType toDomain(JpaTicketType jpa) {
        TicketType tt = new TicketType();
        tt.setId(jpa.getId());
        tt.setName(jpa.getName());
        tt.setPrice(jpa.getPrice());
        tt.setTotalQuantity(jpa.getTotalQuantity());
        tt.setSoldQuantity(jpa.getSoldQuantity());
        tt.setEventId(jpa.getEvent() != null ? jpa.getEvent().getId() : null);
        tt.setSectionId(jpa.getSection() != null ? jpa.getSection().getId() : null);
        tt.setSectionName(jpa.getSection() != null ? jpa.getSection().getName() : null);
        tt.setCreatedAt(jpa.getCreatedAt());
        tt.setUpdatedAt(jpa.getUpdatedAt());
        return tt;
    }
}
