package com.soe.jcb.eventdriven.demo.order.interfaces.web;

import com.soe.jcb.eventdriven.demo.common.interfaces.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.order.application.in.OrderUseCase;
import com.soe.jcb.eventdriven.demo.order.interfaces.dto.OrderCreateRequest;
import com.soe.jcb.eventdriven.demo.order.interfaces.dto.OrderResponse;
import com.soe.jcb.eventdriven.demo.user.application.in.UserQueryPort;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    private final OrderUseCase orderUseCase;
    private final UserQueryPort userQueryPort;

    public OrderController(OrderUseCase orderUseCase, UserQueryPort userQueryPort) {
        this.orderUseCase = orderUseCase;
        this.userQueryPort = userQueryPort;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request,
                                             Principal principal) {
        Long userId = userQueryPort.findByEmail(principal.getName())
                .orElseThrow(() -> new com.soe.jcb.eventdriven.demo.common.domain.exception
                        .ResourceNotFoundException("User", "email", principal.getName()))
                .getId();
        OrderUseCase.OrderResult order = orderUseCase.create(request.toCommand(userId));
        return ApiResponse.ok("Order created successfully", OrderResponse.from(order));
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> findMyOrders(Principal principal) {
        Long userId = userQueryPort.findByEmail(principal.getName())
                .map(u -> u.getId()).orElseThrow();
        return ApiResponse.ok(orderUseCase.findByUser(userId).stream().map(OrderResponse::from).toList());
    }

    @GetMapping("/{orderNumber}")
    public ApiResponse<OrderResponse> findByOrderNumber(@PathVariable String orderNumber,
                                                        Principal principal) {
        Long userId = userQueryPort.findByEmail(principal.getName())
                .map(u -> u.getId()).orElseThrow();
        return ApiResponse.ok(OrderResponse.from(orderUseCase.findByOrderNumber(orderNumber, userId)));
    }

    @PostMapping("/{orderNumber}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable String orderNumber, Principal principal) {
        Long userId = userQueryPort.findByEmail(principal.getName())
                .map(u -> u.getId()).orElseThrow();
        return ApiResponse.ok("Order cancelled",
                OrderResponse.from(orderUseCase.cancel(orderNumber, userId)));
    }
}