package com.soe.jcb.eventdriven.demo.user.dto;

import com.soe.jcb.eventdriven.demo.user.entity.Role;
import com.soe.jcb.eventdriven.demo.user.entity.User;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole()
        );
    }
}
