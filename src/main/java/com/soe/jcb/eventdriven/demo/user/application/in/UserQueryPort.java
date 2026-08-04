package com.soe.jcb.eventdriven.demo.user.application.in;

import com.soe.jcb.eventdriven.demo.user.domain.User;
import java.util.Optional;

/**
 * Query port used by the web layer to resolve the currently authenticated user
 * (from the {@code Principal.name}) and by other contexts that need a user by id.
 */
public interface UserQueryPort {

    Optional<User> findByEmail(String email);

    Optional<User> findById(Long id);
}