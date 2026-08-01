package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.infrastructure.persistence.JpaBaseEntity;
import com.ticketwave.domain.event.model.EventCategory;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.infrastructure.repository.jpa.JpaVenue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
public class JpaEvent extends JpaBaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventCategory category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EventStatus status = EventStatus.DRAFT;

    private String artist;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "sales_start_date")
    private LocalDateTime salesStartDate;

    @Column(name = "sales_end_date")
    private LocalDateTime salesEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private JpaVenue venue;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<JpaTicketType> ticketTypes = new ArrayList<>();

    public JpaEvent() {}

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

    public EventCategory getCategory() {
        return category;
    }

    public void setCategory(EventCategory category) {
        this.category = category;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
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

    public JpaVenue getVenue() {
        return venue;
    }

    public void setVenue(JpaVenue venue) {
        this.venue = venue;
    }

    public List<JpaTicketType> getTicketTypes() {
        return ticketTypes;
    }

    public void setTicketTypes(List<JpaTicketType> ticketTypes) {
        this.ticketTypes = ticketTypes;
    }
}
