package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.UserResponse;
import com.ticketwave.application.mapper.UserMapper;
import com.ticketwave.domain.user.model.User;
import com.ticketwave.application.usecase.FindUserUseCase;
import com.ticketwave.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FindUserUseCaseImpl implements FindUserUseCase {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public FindUserUseCaseImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public UserResponse findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findEntityByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
    }
}
