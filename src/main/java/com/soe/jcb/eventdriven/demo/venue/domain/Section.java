package com.soe.jcb.eventdriven.demo.venue.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Section value/entity of the Venue aggregate. Pure domain - no JPA.
 */
public class Section {

    private Long id;
    private String name;
    private int capacity;
    private boolean generalAdmission;
    private final List<Seat> seats = new ArrayList<>();

    public Section(Long id, String name, int capacity, boolean generalAdmission) {
        this.id = id;
        this.name = name;
        this.capacity = capacity;
        this.generalAdmission = generalAdmission;
    }

    public void addSeat(Seat seat) {
        seats.add(seat);
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

    public int getCapacity() {
        return capacity;
    }

    public boolean isGeneralAdmission() {
        return generalAdmission;
    }

    public List<Seat> getSeats() {
        return seats;
    }
}