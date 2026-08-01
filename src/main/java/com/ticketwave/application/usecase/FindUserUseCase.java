package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.UserResponse;
import com.ticketwave.domain.user.model.User;

public interface FindUserUseCase {
    UserResponse findById(Long id);
    User findEntityByEmail(String email);
}
