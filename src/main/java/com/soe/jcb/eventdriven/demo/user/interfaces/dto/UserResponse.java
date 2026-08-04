package com.soe.jcb.eventdriven.demo.user.interfaces.dto;

import com.soe.jcb.eventdriven.demo.user.application.in.RegisterUserUseCase;
import com.soe.jcb.eventdriven.demo.user.domain.User;

/**
 * User response DTO used by the web layer.
 */
public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        User.Role role,
        boolean enabled
) {
    public static UserResponse from(RegisterUserUseCase.Result result) {
        return new UserResponse(result.id(), result.email(), result.firstName(),
                result.lastName(), result.role(), result.enabled());
    }

    public static UserResponse from(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getRole(), user.isEnabled());
    }
}