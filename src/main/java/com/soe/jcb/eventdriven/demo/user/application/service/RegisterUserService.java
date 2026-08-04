package com.soe.jcb.eventdriven.demo.user.application.service;

import com.soe.jcb.eventdriven.demo.common.domain.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.user.application.in.RegisterUserUseCase;
import com.soe.jcb.eventdriven.demo.user.application.out.PasswordEncoderPort;
import com.soe.jcb.eventdriven.demo.user.domain.User;
import com.soe.jcb.eventdriven.demo.user.domain.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Application service orchestrating the {@code register} use-case.
 *
 * <p>It depends only on the domain {@link UserRepository} port and the
 * {@link PasswordEncoderPort} output port - never on a concrete JPA/Spring
 * implementation. This is dependency inversion at work.
 */
@Service
public class RegisterUserService implements RegisterUserUseCase {

    private final UserRepository userRepository;
    private final PasswordEncoderPort passwordEncoder;

    public RegisterUserService(UserRepository userRepository, PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Result register(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new BusinessRuleException("Email already registered: " + command.email());
        }

        String encoded = passwordEncoder.encode(command.password());
        User user = userRepository.save(new User(
                null, command.email(), encoded, command.firstName(),
                command.lastName(), command.phone(), User.Role.CUSTOMER, true));

        return new Result(user.getId(), user.getEmail(), user.getFirstName(),
                user.getLastName(), user.getRole(), user.isEnabled());
    }
}