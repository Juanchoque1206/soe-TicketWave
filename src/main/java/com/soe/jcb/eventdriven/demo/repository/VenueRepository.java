package com.soe.jcb.eventdriven.demo.repository;

import com.soe.jcb.eventdriven.demo.entity.Venue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenueRepository extends JpaRepository<Venue, Long> {

    List<Venue> findByCity(String city);

    List<Venue> findByNameContainingIgnoreCase(String name);
}
