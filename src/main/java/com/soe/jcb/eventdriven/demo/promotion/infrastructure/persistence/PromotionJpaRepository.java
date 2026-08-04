package com.soe.jcb.eventdriven.demo.promotion.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PromotionJpaRepository extends JpaRepository<PromotionJpaEntity, Long> {

    Optional<PromotionJpaEntity> findByCodeAndActiveTrue(String code);

    List<PromotionJpaEntity> findByActiveTrue();

    @Modifying
    @Query("UPDATE PromotionJpaEntity p SET p.currentUses = p.currentUses + 1 " +
            "WHERE p.id = :id AND p.currentUses < p.maxUses")
    int incrementCurrentUses(@Param("id") Long id);
}