package com.soe.jcb.eventdriven.demo.user.domain;

import java.util.Optional;

/**
 * Domain port (outbound / secondary interface) for persisting {@link User}.
 *
 * <p>Defined in the domain layer to express the contract the application needs
 * without any knowledge of how it is implemented. The concrete Spring Data
 * adapter lives in {@code user.infrastructure.persistence}.
 */
public interface UserRepository {

    User save(User user);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}