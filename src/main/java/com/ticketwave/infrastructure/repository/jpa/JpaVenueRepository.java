package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JpaVenueRepository extends JpaRepository<JpaVenue, Long> {
    List<JpaVenue> findByCity(String city);
    List<JpaVenue> findByNameContainingIgnoreCase(String name);
}
