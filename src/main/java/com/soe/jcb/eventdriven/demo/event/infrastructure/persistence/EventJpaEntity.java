package com.soe.jcb.eventdriven.demo.event.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.common.infrastructure.persistence.BaseJpaEntity;
import com.soe.jcb.eventdriven.demo.event.domain.Event;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Index;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events", indexes = {
        @Index(name = "idx_event_artist", columnList = "artist"),
        @Index(name = "idx_event_date", columnList = "event_date"),
        @Index(name = "idx_event_status", columnList = "status")
})
public class EventJpaEntity extends BaseJpaEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Event.Category category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Event.Status status = Event.Status.DRAFT;

    private String artist;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "sales_start_date")
    private LocalDateTime salesStartDate;

    @Column(name = "sales_end_date")
    private LocalDateTime salesEndDate;

    private Long venueId;

    @OneToMany(mappedBy = "eventId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TicketTypeJpaEntity> ticketTypes = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Event.Category getCategory() {
        return category;
    }

    public void setCategory(Event.Category category) {
        this.category = category;
    }

    public Event.Status getStatus() {
        return status;
    }

    public void setStatus(Event.Status status) {
        this.status = status;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public LocalDateTime getSalesStartDate() {
        return salesStartDate;
    }

    public void setSalesStartDate(LocalDateTime salesStartDate) {
        this.salesStartDate = salesStartDate;
    }

    public LocalDateTime getSalesEndDate() {
        return salesEndDate;
    }

    public void setSalesEndDate(LocalDateTime salesEndDate) {
        this.salesEndDate = salesEndDate;
    }

    public Long getVenueId() {
        return venueId;
    }

    public void setVenueId(Long venueId) {
        this.venueId = venueId;
    }

    public List<TicketTypeJpaEntity> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<TicketTypeJpaEntity> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}