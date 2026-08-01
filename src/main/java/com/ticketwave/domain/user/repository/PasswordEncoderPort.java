package com.ticketwave.domain.user.repository;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
}
