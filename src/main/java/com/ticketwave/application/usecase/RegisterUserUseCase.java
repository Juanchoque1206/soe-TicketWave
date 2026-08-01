package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.UserRegistrationRequest;
import com.ticketwave.application.dto.UserResponse;

public interface RegisterUserUseCase {
    UserResponse register(UserRegistrationRequest request);
}
