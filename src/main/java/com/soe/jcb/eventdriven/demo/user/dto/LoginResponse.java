package com.soe.jcb.eventdriven.demo.user.dto;

import com.soe.jcb.eventdriven.demo.user.entity.Role;

public record LoginResponse(
        Long userId,
        String email,
        Role role,
        String message
) {
}
