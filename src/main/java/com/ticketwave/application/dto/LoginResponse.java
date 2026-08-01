package com.ticketwave.application.dto;

import com.ticketwave.domain.user.model.Role;

public record LoginResponse(
        Long userId,
        String email,
        Role role,
        String message
) {}
