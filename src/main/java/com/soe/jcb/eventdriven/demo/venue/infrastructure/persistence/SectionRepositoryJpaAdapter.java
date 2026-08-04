package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import org.springframework.stereotype.Component;

import java.util.Optional;

import com.soe.jcb.eventdriven.demo.venue.domain.Section;
import com.soe.jcb.eventdriven.demo.venue.domain.SectionRepository;

@Component
public class SectionRepositoryJpaAdapter implements SectionRepository {

    private final SectionJpaRepository jpaRepository;

    public SectionRepositoryJpaAdapter(SectionJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Optional<Section> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    private Section toDomain(SectionJpaEntity entity) {
        Section section = new Section(entity.getId(), entity.getName(),
                entity.getCapacity(), entity.isGeneralAdmission());
        if (entity.getSeats() != null) {
            for (SeatJpaEntity seat : entity.getSeats()) {
                section.addSeat(new SeatMapper().toDomain(seat));
            }
        }
        return section;
    }
}