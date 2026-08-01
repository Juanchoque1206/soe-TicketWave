package com.ticketwave.infrastructure.repository.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<JpaUser, Long> {
    Optional<JpaUser> findByEmail(String email);
    boolean existsByEmail(String email);
}
