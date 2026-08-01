package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaSeatRepository extends JpaRepository<JpaSeat, Long> {
    List<JpaSeat> findBySectionId(Long sectionId);
}
