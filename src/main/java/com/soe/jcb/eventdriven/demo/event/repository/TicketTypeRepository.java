package com.soe.jcb.eventdriven.demo.event.repository;

import com.soe.jcb.eventdriven.demo.event.entity.TicketType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TicketTypeRepository extends JpaRepository<TicketType, Long> {

    List<TicketType> findByEventId(Long eventId);

    @Modifying
    @Query("UPDATE TicketType t SET t.soldQuantity = t.soldQuantity + :qty " +
            "WHERE t.id = :id AND (t.soldQuantity + :qty) <= t.totalQuantity")
    int incrementSoldQuantity(@Param("id") Long id, @Param("qty") int qty);

    @Modifying
    @Query("UPDATE TicketType t SET t.soldQuantity = t.soldQuantity - :qty " +
            "WHERE t.id = :id AND t.soldQuantity >= :qty")
    int decrementSoldQuantity(@Param("id") Long id, @Param("qty") int qty);
}
