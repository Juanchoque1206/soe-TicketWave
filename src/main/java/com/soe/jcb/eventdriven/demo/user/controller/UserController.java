package com.soe.jcb.eventdriven.demo.user.controller;

import com.soe.jcb.eventdriven.demo.common.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.user.dto.UserRegistrationRequest;
import com.soe.jcb.eventdriven.demo.user.dto.UserResponse;
import com.soe.jcb.eventdriven.demo.user.entity.User;
import com.soe.jcb.eventdriven.demo.user.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/api/v1/auth/register")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<UserResponse> register(@Valid @RequestBody UserRegistrationRequest request) {
        UserResponse user = userService.register(request);
        return ApiResponse.ok("User registered successfully", user);
    }

    @GetMapping("/api/v1/users/me")
    public ApiResponse<UserResponse> getCurrentUser(Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        return ApiResponse.ok(UserResponse.from(user));
    }
}
