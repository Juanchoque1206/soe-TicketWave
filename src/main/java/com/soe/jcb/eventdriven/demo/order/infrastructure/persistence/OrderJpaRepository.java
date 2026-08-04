package com.soe.jcb.eventdriven.demo.order.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface OrderJpaRepository extends JpaRepository<OrderJpaEntity, Long> {

    List<OrderJpaEntity> findByUserIdOrderByCreatedAtDesc(Long userId);

    Optional<OrderJpaEntity> findByOrderNumber(String orderNumber);

    @Query("SELECT COUNT(o) FROM OrderJpaEntity o WHERE o.userId = :userId " +
            "AND o.status = :status AND o.createdAt >= :after")
    long countByUserIdAndStatusAndCreatedAtAfter(@Param("userId") Long userId,
                                                 @Param("status") OrderStatus status,
                                                 @Param("after") LocalDateTime after);
}