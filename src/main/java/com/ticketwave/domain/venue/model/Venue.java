package com.ticketwave.domain.venue.model;

import com.ticketwave.domain.common.model.BaseEntity;

import java.util.ArrayList;
import java.util.List;

public class Venue extends BaseEntity {

    private String name;
    private String city;
    private String address;
    private int capacity;
    private boolean hasAssignedSeating;
    private List<Section> sections = new ArrayList<>();

    public Venue() {}

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public boolean isHasAssignedSeating() { return hasAssignedSeating; }
    public void setHasAssignedSeating(boolean hasAssignedSeating) { this.hasAssignedSeating = hasAssignedSeating; }

    public List<Section> getSections() { return sections; }
    public void setSections(List<Section> sections) { this.sections = sections; }
}
