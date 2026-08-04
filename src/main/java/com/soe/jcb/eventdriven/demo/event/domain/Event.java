package com.soe.jcb.eventdriven.demo.event.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Event aggregate root (pure domain - no annotations).
 *
 * <p>References the venue by id (venueId) rather than holding a cross-context
 * object reference, keeping the event context decoupled from the venue context
 * in line with bounded-context isolation.
 */
public class Event {

    public enum Category {
        CONCERT, SPORTS, CONFERENCE, THEATER, FESTIVAL, OTHER
    }

    public enum Status {
        DRAFT, PUBLISHED, POSTPONED, CANCELLED, COMPLETED
    }

    private Long id;
    private String name;
    private String description;
    private Category category;
    private Status status;
    private String artist;
    private LocalDateTime eventDate;
    private LocalDateTime salesStartDate;
    private LocalDateTime salesEndDate;
    private Long venueId;
    private final List<TicketType> ticketTypes = new ArrayList<>();

    public Event(Long id, String name, String description, Category category,
                 String artist, LocalDateTime eventDate, LocalDateTime salesStartDate,
                 LocalDateTime salesEndDate, Long venueId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.category = category;
        this.status = Status.DRAFT;
        this.artist = artist;
        this.eventDate = eventDate;
        this.salesStartDate = salesStartDate;
        this.salesEndDate = salesEndDate;
        this.venueId = venueId;
    }

    public void addTicketType(TicketType ticketType) {
        ticketTypes.add(ticketType);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Category getCategory() {
        return category;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getArtist() {
        return artist;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public LocalDateTime getSalesStartDate() {
        return salesStartDate;
    }

    public LocalDateTime getSalesEndDate() {
        return salesEndDate;
    }

    public Long getVenueId() {
        return venueId;
    }

    public List<TicketType> getTicketTypes() {
        return ticketTypes;
    }
}