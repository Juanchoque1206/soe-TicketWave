package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Role;

public record LoginResponse(
        Long userId,
        String email,
        Role role,
        String message
) {
}
