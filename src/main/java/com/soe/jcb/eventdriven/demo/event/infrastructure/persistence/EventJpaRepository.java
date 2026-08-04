package com.soe.jcb.eventdriven.demo.event.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.event.domain.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Collection;

public interface EventJpaRepository extends JpaRepository<EventJpaEntity, Long> {

    @Query("SELECT e FROM EventJpaEntity e WHERE e.status = :status " +
            "AND (:venueIds IS NULL OR e.venueId IN :venueIds) " +
            "AND (:artist IS NULL OR LOWER(e.artist) LIKE LOWER(CONCAT('%', :artist, '%'))) " +
            "AND (:venueId IS NULL OR e.venueId = :venueId) " +
            "AND (:fromDate IS NULL OR e.eventDate >= :fromDate) " +
            "AND (:toDate IS NULL OR e.eventDate <= :toDate)")
    Page<EventJpaEntity> searchEvents(@Param("status") Event.Status status,
                                      @Param("venueIds") Collection<Long> venueIds,
                                      @Param("artist") String artist,
                                      @Param("venueId") Long venueId,
                                      @Param("fromDate") LocalDateTime fromDate,
                                      @Param("toDate") LocalDateTime toDate,
                                      Pageable pageable);
}