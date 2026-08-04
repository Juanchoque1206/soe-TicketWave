package com.soe.jcb.eventdriven.demo.venue.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SeatJpaRepository extends JpaRepository<SeatJpaEntity, Long> {
}