package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.venue.model.Seat;
import com.ticketwave.domain.venue.model.Section;
import com.ticketwave.domain.venue.model.Venue;
import org.springframework.stereotype.Component;

@Component
public class VenuePersistenceMapper {

    public Venue toDomain(JpaVenue jpa) {
        Venue venue = new Venue();
        venue.setId(jpa.getId());
        venue.setName(jpa.getName());
        venue.setCity(jpa.getCity());
        venue.setAddress(jpa.getAddress());
        venue.setCapacity(jpa.getCapacity());
        venue.setHasAssignedSeating(jpa.isHasAssignedSeating());
        venue.setCreatedAt(jpa.getCreatedAt());
        venue.setUpdatedAt(jpa.getUpdatedAt());
        if (jpa.getSections() != null) {
            venue.setSections(jpa.getSections().stream().map(this::toDomain).toList());
        }
        return venue;
    }

    public Section toDomain(JpaSection jpa) {
        Section section = new Section();
        section.setId(jpa.getId());
        section.setName(jpa.getName());
        section.setCapacity(jpa.getCapacity());
        section.setGeneralAdmission(jpa.isGeneralAdmission());
        section.setVenueId(jpa.getVenue() != null ? jpa.getVenue().getId() : null);
        section.setCreatedAt(jpa.getCreatedAt());
        section.setUpdatedAt(jpa.getUpdatedAt());
        if (jpa.getSeats() != null) {
            section.setSeats(jpa.getSeats().stream().map(this::toDomain).toList());
        }
        return section;
    }

    public Seat toDomain(JpaSeat jpa) {
        Seat seat = new Seat();
        seat.setId(jpa.getId());
        seat.setRow(jpa.getRow());
        seat.setNumber(jpa.getNumber());
        seat.setSectionId(jpa.getSection() != null ? jpa.getSection().getId() : null);
        seat.setCreatedAt(jpa.getCreatedAt());
        seat.setUpdatedAt(jpa.getUpdatedAt());
        return seat;
    }

    public JpaVenue toJpa(Venue domain) {
        JpaVenue jpa = new JpaVenue();
        jpa.setId(domain.getId());
        jpa.setName(domain.getName());
        jpa.setCity(domain.getCity());
        jpa.setAddress(domain.getAddress());
        jpa.setCapacity(domain.getCapacity());
        jpa.setHasAssignedSeating(domain.isHasAssignedSeating());
        if (domain.getSections() != null) {
            for (Section section : domain.getSections()) {
                JpaSection jpaSection = toJpaSection(section);
                jpaSection.setVenue(jpa);
                for (JpaSeat jpaSeat : jpaSection.getSeats()) {
                    jpaSeat.setSection(jpaSection);
                }
                jpa.getSections().add(jpaSection);
            }
        }
        return jpa;
    }

    private JpaSection toJpaSection(Section domain) {
        JpaSection jpa = new JpaSection();
        jpa.setId(domain.getId());
        jpa.setName(domain.getName());
        jpa.setCapacity(domain.getCapacity());
        jpa.setGeneralAdmission(domain.isGeneralAdmission());
        if (domain.getSeats() != null) {
            for (Seat seat : domain.getSeats()) {
                JpaSeat jpaSeat = toJpaSeat(seat);
                jpa.getSeats().add(jpaSeat);
            }
        }
        return jpa;
    }

    private JpaSeat toJpaSeat(Seat domain) {
        JpaSeat jpa = new JpaSeat();
        jpa.setId(domain.getId());
        jpa.setRow(domain.getRow());
        jpa.setNumber(domain.getNumber());
        return jpa;
    }
}
