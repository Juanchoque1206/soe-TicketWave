package com.ticketwave.domain.venue.model;

import com.ticketwave.domain.common.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class Section extends BaseEntity {

    private String name;
    private int capacity;
    private boolean generalAdmission;
    private Long venueId;
    private List<Seat> seats = new ArrayList<>();

    public Section() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isGeneralAdmission() { return generalAdmission; }
    public void setGeneralAdmission(boolean generalAdmission) { this.generalAdmission = generalAdmission; }

    public Long getVenueId() { return venueId; }
    public void setVenueId(Long venueId) { this.venueId = venueId; }

    public List<Seat> getSeats() { return seats; }
    public void setSeats(List<Seat> seats) { this.seats = seats; }
}
