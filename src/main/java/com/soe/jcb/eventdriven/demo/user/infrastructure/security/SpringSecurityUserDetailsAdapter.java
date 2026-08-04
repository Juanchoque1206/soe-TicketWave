package com.soe.jcb.eventdriven.demo.user.infrastructure.security;

import com.soe.jcb.eventdriven.demo.user.application.in.UserQueryPort;
import com.soe.jcb.eventdriven.demo.user.domain.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Anti-corruption adapter between Spring Security and the user context.
 *
 * <p>Spring Security requires a {@link UserDetailsService}; the user domain
 * model knows nothing about it. This adapter (infrastructure) maps a domain
 * {@link User} to Spring Security's {@link UserDetails}.
 */
@Component
public class SpringSecurityUserDetailsAdapter implements UserDetailsService {

    private final UserQueryPort userQueryPort;

    public SpringSecurityUserDetailsAdapter(UserQueryPort userQueryPort) {
        this.userQueryPort = userQueryPort;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userQueryPort.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                user.isEnabled(),
                true, true, true,
                List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
        );
    }
}