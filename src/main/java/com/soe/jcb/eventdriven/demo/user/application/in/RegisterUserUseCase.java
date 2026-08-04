package com.soe.jcb.eventdriven.demo.user.application.in;

import com.soe.jcb.eventdriven.demo.user.domain.User;

/**
 * Input use-case for user registration.
 *
 * <p>Commands and results are records, keeping the application boundary free
 * of domain persistence details and web framework types.
 */
public interface RegisterUserUseCase {

    Result register(RegisterUserCommand command);

    record RegisterUserCommand(String email, String password, String firstName,
                               String lastName, String phone) {
    }

    record Result(Long id, String email, String firstName, String lastName, User.Role role, boolean enabled) {
    }
}