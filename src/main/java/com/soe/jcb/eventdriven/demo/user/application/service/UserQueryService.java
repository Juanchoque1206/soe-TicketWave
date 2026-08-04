package com.soe.jcb.eventdriven.demo.user.application.service;

import com.soe.jcb.eventdriven.demo.user.application.in.UserQueryPort;
import com.soe.jcb.eventdriven.demo.user.domain.User;
import com.soe.jcb.eventdriven.demo.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Read-only application service exposing user lookups to the interface layer
 * and to other contexts (through their own ports/adapters).
 */
@Service
public class UserQueryService implements UserQueryPort {

    private final UserRepository userRepository;

    public UserQueryService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }
}