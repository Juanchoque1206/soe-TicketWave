package com.soe.jcb.eventdriven.demo.user.infrastructure.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data repository - a persistence detail. The domain port
 * {@code user.domain.UserRepository} is implemented by {@link UserRepositoryJpaAdapter}.
 */
public interface UserJpaRepository extends JpaRepository<UserJpaEntity, Long> {

    Optional<UserJpaEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}