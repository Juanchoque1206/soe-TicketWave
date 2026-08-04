package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.common.infrastructure.persistence.BaseJpaEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections")
public class SectionJpaEntity extends BaseJpaEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int capacity;

    @Column(name = "is_general_admission", nullable = false)
    private boolean generalAdmission;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "venue_id", nullable = false)
    private VenueJpaEntity venue;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SeatJpaEntity> seats = new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public boolean isGeneralAdmission() {
        return generalAdmission;
    }

    public void setGeneralAdmission(boolean generalAdmission) {
        this.generalAdmission = generalAdmission;
    }

    public VenueJpaEntity getVenue() {
        return venue;
    }

    public void setVenue(VenueJpaEntity venue) {
        this.venue = venue;
    }

    public List<SeatJpaEntity> getSeats() {
        return seats;
    }

    public void setSeats(List<SeatJpaEntity> seats) {
        this.seats = seats;
    }
}