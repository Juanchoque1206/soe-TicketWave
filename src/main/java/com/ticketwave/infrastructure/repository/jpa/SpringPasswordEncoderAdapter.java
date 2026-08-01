package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.user.repository.PasswordEncoderPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class SpringPasswordEncoderAdapter implements PasswordEncoderPort {

    private final PasswordEncoder passwordEncoder;

    public SpringPasswordEncoderAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}
