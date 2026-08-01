package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.application.usecase.AuthenticateUserUseCase;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserDetailsServiceAdapter implements UserDetailsService, AuthenticateUserUseCase {

    private final JpaUserRepository jpaUserRepository;

    public UserDetailsServiceAdapter(JpaUserRepository jpaUserRepository) {
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        JpaUser user = jpaUserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                user.isEnabled(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
    }
}
