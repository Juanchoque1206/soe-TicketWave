package com.soe.jcb.eventdriven.demo.event.repository;

import com.soe.jcb.eventdriven.demo.event.entity.Event;
import com.soe.jcb.eventdriven.demo.event.entity.EventStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface EventRepository extends JpaRepository<Event, Long> {

    @Query("SELECT e FROM Event e WHERE e.status = :status " +
            "AND (:city IS NULL OR LOWER(e.venue.city) = LOWER(:city)) " +
            "AND (:artist IS NULL OR LOWER(e.artist) LIKE LOWER(CONCAT('%', :artist, '%'))) " +
            "AND (:venueId IS NULL OR e.venue.id = :venueId) " +
            "AND (:fromDate IS NULL OR e.eventDate >= :fromDate) " +
            "AND (:toDate IS NULL OR e.eventDate <= :toDate)")
    Page<Event> searchEvents(@Param("status") EventStatus status,
                             @Param("city") String city,
                             @Param("artist") String artist,
                             @Param("venueId") Long venueId,
                             @Param("fromDate") LocalDateTime fromDate,
                             @Param("toDate") LocalDateTime toDate,
                             Pageable pageable);
}
