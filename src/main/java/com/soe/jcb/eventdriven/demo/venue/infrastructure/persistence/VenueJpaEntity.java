package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.common.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "venues")
public class VenueJpaEntity extends BaseJpaEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "has_assigned_seating", nullable = false)
    private boolean hasAssignedSeating;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SectionJpaEntity> sections = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isHasAssignedSeating() {
        return hasAssignedSeating;
    }

    public void setHasAssignedSeating(boolean hasAssignedSeating) {
        this.hasAssignedSeating = hasAssignedSeating;
    }

    public List<SectionJpaEntity> getSections() {
        return sections;
    }

    public void setSections(List<SectionJpaEntity> sections) {
        this.sections = sections;
    }
}