package com.ticketwave.domain.antifraud.repository;

import java.time.LocalDateTime;

public interface OrderActivityPort {
    long countPendingOrdersSince(Long userId, LocalDateTime since);
}
