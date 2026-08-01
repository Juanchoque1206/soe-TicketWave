package com.ticketwave.application.mapper;

import com.ticketwave.application.dto.UserResponse;
import com.ticketwave.domain.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole());
    }
}
