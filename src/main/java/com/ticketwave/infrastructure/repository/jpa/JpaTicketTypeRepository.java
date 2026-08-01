package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaTicketTypeRepository extends JpaRepository<JpaTicketType, Long> {

    List<JpaTicketType> findByEventId(Long eventId);

    @Modifying
    @Query("UPDATE JpaTicketType t SET t.soldQuantity = t.soldQuantity + :qty " +
            "WHERE t.id = :id AND (t.soldQuantity + :qty) <= t.totalQuantity")
    int incrementSoldQuantity(@Param("id") Long id, @Param("qty") int qty);

    @Modifying
    @Query("UPDATE JpaTicketType t SET t.soldQuantity = t.soldQuantity - :qty " +
            "WHERE t.id = :id AND t.soldQuantity >= :qty")
    int decrementSoldQuantity(@Param("id") Long id, @Param("qty") int qty);
}
