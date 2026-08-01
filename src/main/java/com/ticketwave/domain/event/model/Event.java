package com.ticketwave.domain.event.model;

import com.ticketwave.domain.common.model.BaseEntity;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Event extends BaseEntity {
    private String name;
    private String description;
    private EventCategory category;
    private EventStatus status = EventStatus.DRAFT;
    private String artist;
    private LocalDateTime eventDate;
    private LocalDateTime salesStartDate;
    private LocalDateTime salesEndDate;
    private Long venueId;
    private String venueName;
    private String venueCity;
    private List<TicketType> ticketTypes = new ArrayList<>();

    public Event() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public EventCategory getCategory() { return category; }
    public void setCategory(EventCategory category) { this.category = category; }
    public EventStatus getStatus() { return status; }
    public void setStatus(EventStatus status) { this.status = status; }
    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }
    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }
    public LocalDateTime getSalesStartDate() { return salesStartDate; }
    public void setSalesStartDate(LocalDateTime salesStartDate) { this.salesStartDate = salesStartDate; }
    public LocalDateTime getSalesEndDate() { return salesEndDate; }
    public void setSalesEndDate(LocalDateTime salesEndDate) { this.salesEndDate = salesEndDate; }
    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }
    public String getVenueName() { return venueName; }
    public void setVenueName(String venueName) { this.venueName = venueName; }
    public String getVenueCity() { return venueCity; }
    public void setVenueCity(String venueCity) { this.venueCity = venueCity; }
    public List<TicketType> getTicketTypes() { return ticketTypes; }
    public void setTicketTypes(List<TicketType> ticketTypes) { this.ticketTypes = ticketTypes; }
}
