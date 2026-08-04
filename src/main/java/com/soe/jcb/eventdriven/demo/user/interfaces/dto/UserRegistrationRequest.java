package com.soe.jcb.eventdriven.demo.user.interfaces.dto;

import com.soe.jcb.eventdriven.demo.user.application.in.RegisterUserUseCase;

/**
 * Web request DTO for registration. Validation annotations are Jakarta
 * Validation - a framework concern that belongs in the interface layer.
 */
public record UserRegistrationRequest(
        String email,
        String password,
        String firstName,
        String lastName,
        String phone
) {
    public RegisterUserUseCase.RegisterUserCommand toCommand() {
        return new RegisterUserUseCase.RegisterUserCommand(email, password, firstName, lastName, phone);
    }
}