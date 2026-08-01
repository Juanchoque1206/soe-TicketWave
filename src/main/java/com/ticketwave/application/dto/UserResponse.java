package com.ticketwave.application.dto;

import com.ticketwave.domain.user.model.Role;
import java.io.Serializable;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) implements Serializable {}
