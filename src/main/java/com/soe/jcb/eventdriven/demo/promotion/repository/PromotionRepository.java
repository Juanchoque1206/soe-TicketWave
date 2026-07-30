package com.soe.jcb.eventdriven.demo.promotion.repository;

import com.soe.jcb.eventdriven.demo.promotion.entity.Promotion;
import com.soe.jcb.eventdriven.demo.promotion.entity.PromotionScope;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    Optional<Promotion> findByCodeAndActiveTrue(String code);

    List<Promotion> findByVenueIdAndActiveTrue(Long venueId);

    List<Promotion> findByScopeAndActiveTrue(PromotionScope scope);

    List<Promotion> findByActiveTrue();

    @Modifying
    @Query("UPDATE Promotion p SET p.currentUses = p.currentUses + 1 " +
            "WHERE p.id = :id AND p.currentUses < p.maxUses")
    int incrementCurrentUses(@Param("id") Long id);
}
