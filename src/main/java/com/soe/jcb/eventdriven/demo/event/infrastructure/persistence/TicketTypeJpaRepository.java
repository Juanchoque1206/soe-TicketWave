package com.soe.jcb.eventdriven.demo.event.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TicketTypeJpaRepository extends JpaRepository<TicketTypeJpaEntity, Long> {

    List<TicketTypeJpaEntity> findByEventId(Long eventId);

    Optional<TicketTypeJpaEntity> findById(Long id);

    @Modifying
    @Query("UPDATE TicketTypeJpaEntity tt SET tt.soldQuantity = tt.soldQuantity + :qty " +
            "WHERE tt.id = :id AND tt.soldQuantity + :qty <= tt.totalQuantity")
    int reserve(@Param("id") Long id, @Param("qty") int qty);

    @Modifying
    @Query("UPDATE TicketTypeJpaEntity tt SET tt.soldQuantity = tt.soldQuantity - :qty " +
            "WHERE tt.id = :id")
    int release(@Param("id") Long id, @Param("qty") int qty);
}