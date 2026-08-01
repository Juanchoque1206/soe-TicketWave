package com.ticketwave.interfaces.rest;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.application.dto.UserRegistrationRequest;
import com.ticketwave.application.dto.UserResponse;
import com.ticketwave.application.mapper.UserMapper;
import com.ticketwave.domain.user.model.User;
import com.ticketwave.application.usecase.FindUserUseCase;
import com.ticketwave.application.usecase.RegisterUserUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final FindUserUseCase findUserUseCase;
    private final UserMapper userMapper;

    public UserController(RegisterUserUseCase registerUserUseCase,
                         FindUserUseCase findUserUseCase,
                         UserMapper userMapper) {
        this.registerUserUseCase = registerUserUseCase;
        this.findUserUseCase = findUserUseCase;
        this.userMapper = userMapper;
    }

    @PostMapping("/api/v1/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse user = registerUserUseCase.register(request);
        return ApiResponse.ok("User registered successfully", user);
    }

    @GetMapping("/api/v1/users/me")
    public ApiResponse<UserResponse> getCurrentUser(Principal principal) {
        User user = findUserUseCase.findEntityByEmail(principal.getName());
        return ApiResponse.ok(userMapper.toResponse(user));
    }
}
