package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSectionRepository extends JpaRepository<JpaSection, Long> {
    List<JpaSection> findByVenueId(Long venueId);
}
