package com.soe.jcb.eventdriven.demo.venue.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Venue aggregate root (pure domain - no annotations).
 */
public class Venue {

    private Long id;
    private String name;
    private String city;
    private String address;
    private int capacity;
    private boolean hasAssignedSeating;
    private final List<Section> sections = new ArrayList<>();

    public Venue(Long id, String name, String city, String address,
                 int capacity, boolean hasAssignedSeating) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.address = address;
        this.capacity = capacity;
        this.hasAssignedSeating = hasAssignedSeating;
    }

    public void addSection(Section section) {
        sections.add(section);
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

    public String getCity() {
        return city;
    }

    public String getAddress() {
        return address;
    }

    public int getCapacity() {
        return capacity;
    }

    public boolean isHasAssignedSeating() {
        return hasAssignedSeating;
    }

    public List<Section> getSections() {
        return sections;
    }
}