package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueJpaRepository extends JpaRepository<VenueJpaEntity, Long> {

    List<VenueJpaEntity> findByCity(String city);
}