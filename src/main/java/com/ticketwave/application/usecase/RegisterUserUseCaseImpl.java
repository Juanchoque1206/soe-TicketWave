package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.application.dto.UserRegistrationRequest;
import com.ticketwave.application.dto.UserResponse;
import com.ticketwave.application.mapper.UserMapper;
import com.ticketwave.domain.user.model.Role;
import com.ticketwave.domain.user.model.User;
import com.ticketwave.application.usecase.RegisterUserUseCase;
import com.ticketwave.domain.user.repository.PasswordEncoderPort;
import com.ticketwave.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final UserMapper userMapper;

    public RegisterUserUseCaseImpl(UserRepository userRepository,
                                    PasswordEncoderPort passwordEncoder,
                                    UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.userMapper = userMapper;
    }

    @Override
    @Transactional
    public UserResponse register(UserRegistrationRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BusinessRuleException("Email already registered: " + request.email());
        }

        User user = new User();
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setPhone(request.phone());
        user.setRole(Role.CUSTOMER);

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }
}
