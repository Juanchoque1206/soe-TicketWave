package com.soe.jcb.eventdriven.demo.venue.repository;

import com.soe.jcb.eventdriven.demo.venue.entity.Seat;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeatRepository extends JpaRepository<Seat, Long> {

    List<Seat> findBySectionId(Long sectionId);
}
