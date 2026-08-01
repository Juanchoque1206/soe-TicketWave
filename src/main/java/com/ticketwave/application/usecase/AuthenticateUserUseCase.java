package com.ticketwave.application.usecase;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthenticateUserUseCase {
    UserDetails loadUserByUsername(String email);
}
