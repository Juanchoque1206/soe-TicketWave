package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.venue.domain.Section;
import com.soe.jcb.eventdriven.demo.venue.domain.Venue;
import com.soe.jcb.eventdriven.demo.venue.domain.VenueRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VenueRepositoryJpaAdapter implements VenueRepository {

    private final VenueJpaRepository jpaRepository;
    private final SectionJpaRepository sectionJpaRepository;

    public VenueRepositoryJpaAdapter(VenueJpaRepository jpaRepository, SectionJpaRepository sectionJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.sectionJpaRepository = sectionJpaRepository;
    }

    @Override
    public Venue save(Venue venue) {
        VenueJpaEntity entity = toJpa(venue);
        VenueJpaEntity saved = jpaRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<Venue> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Venue> findByCity(String city) {
        return jpaRepository.findByCity(city).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Venue> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    private VenueJpaEntity toJpa(Venue venue) {
        VenueJpaEntity entity = new VenueJpaEntity();
        entity.setId(venue.getId());
        entity.setName(venue.getName());
        entity.setCity(venue.getCity());
        entity.setAddress(venue.getAddress());
        entity.setCapacity(venue.getCapacity());
        entity.setHasAssignedSeating(venue.isHasAssignedSeating());
        for (Section section : venue.getSections()) {
            SectionJpaEntity sectionEntity = new SectionJpaEntity();
            sectionEntity.setId(section.getId());
            sectionEntity.setName(section.getName());
            sectionEntity.setCapacity(section.getCapacity());
            sectionEntity.setGeneralAdmission(section.isGeneralAdmission());
            sectionEntity.setVenue(entity);
            section.getSeats().forEach(seat -> {
                SeatJpaEntity seatEntity = new SeatJpaEntity();
                seatEntity.setId(seat.getId());
                seatEntity.setRow(seat.getRow());
                seatEntity.setNumber(seat.getNumber());
                seatEntity.setSection(sectionEntity);
                sectionEntity.getSeats().add(seatEntity);
            });
            entity.getSections().add(sectionEntity);
        }
        return entity;
    }

    private Venue toDomain(VenueJpaEntity entity) {
        Venue venue = new Venue(entity.getId(), entity.getName(), entity.getCity(),
                entity.getAddress(), entity.getCapacity(), entity.isHasAssignedSeating());
        if (entity.getSections() != null) {
            for (SectionJpaEntity sectionEntity : entity.getSections()) {
                Section section = new Section(sectionEntity.getId(), sectionEntity.getName(),
                        sectionEntity.getCapacity(), sectionEntity.isGeneralAdmission());
                if (sectionEntity.getSeats() != null) {
                    sectionEntity.getSeats().forEach(seat -> section.addSeat(new SeatMapper().toDomain(seat)));
                }
                venue.addSection(section);
            }
        }
        return venue;
    }
}