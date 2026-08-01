package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaPromotionRepository extends JpaRepository<JpaPromotion, Long> {

    Optional<JpaPromotion> findByCodeAndActiveTrue(String code);

    List<JpaPromotion> findByActiveTrue();

    @Modifying
    @Query("UPDATE JpaPromotion p SET p.currentUses = p.currentUses + 1 " +
            "WHERE p.id = :id AND p.currentUses < p.maxUses")
    int incrementCurrentUses(@Param("id") Long id);
}
