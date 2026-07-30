package com.soe.jcb.eventdriven.demo.repository;

import com.soe.jcb.eventdriven.demo.entity.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SectionRepository extends JpaRepository<Section, Long> {

    List<Section> findByVenueId(Long venueId);
}
