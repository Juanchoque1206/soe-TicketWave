package com.soe.jcb.eventdriven.demo.venue.repository;

import com.soe.jcb.eventdriven.demo.venue.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByVenueId(Long venueId);
}
