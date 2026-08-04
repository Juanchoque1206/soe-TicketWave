package com.soe.jcb.eventdriven.demo.user.interfaces.web;

import com.soe.jcb.eventdriven.demo.common.domain.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.common.interfaces.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.user.application.in.RegisterUserUseCase;
import com.soe.jcb.eventdriven.demo.user.application.in.UserQueryPort;
import com.soe.jcb.eventdriven.demo.user.domain.User;
import com.soe.jcb.eventdriven.demo.user.interfaces.dto.UserRegistrationRequest;
import com.soe.jcb.eventdriven.demo.user.interfaces.dto.UserResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * Web (interface) adapter for the user context.
 *
 * <p>Responsibilities are limited to HTTP concerns: parsing requests,
 * invoking application use-cases, and translating results into {@link ApiResponse}.
 */
@RestController
public class UserController {

    private final RegisterUserUseCase registerUserUseCase;
    private final UserQueryPort userQueryPort;

    public UserController(RegisterUserUseCase registerUserUseCase, UserQueryPort userQueryPort) {
        this.registerUserUseCase = registerUserUseCase;
        this.userQueryPort = userQueryPort;
    }

    @PostMapping("/api/v1/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        RegisterUserUseCase.Result result = registerUserUseCase.register(request.toCommand());
        return ApiResponse.ok("User registered successfully", UserResponse.from(result));
    }

    @GetMapping("/api/v1/users/me")
    public ApiResponse<UserResponse> getCurrentUser(Principal principal) {
        User user = userQueryPort.findByEmail(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", principal.getName()));
        return ApiResponse.ok(UserResponse.from(user));
    }
}