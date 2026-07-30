package com.soe.jcb.eventdriven.demo.order.controller;

import com.soe.jcb.eventdriven.demo.common.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.order.dto.OrderCreateRequest;
import com.soe.jcb.eventdriven.demo.order.dto.OrderResponse;
import com.soe.jcb.eventdriven.demo.user.entity.User;
import com.soe.jcb.eventdriven.demo.order.service.OrderService;
import com.soe.jcb.eventdriven.demo.user.service.UserService;
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

    private final OrderService orderService;
    private final UserService userService;

    public OrderController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request,
                                              Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        OrderResponse order = orderService.createOrder(user.getId(), request);
        return ApiResponse.ok("Order created successfully", order);
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> findMyOrders(Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        return ApiResponse.ok(orderService.findByUser(user.getId()));
    }

    @GetMapping("/{orderNumber}")
    public ApiResponse<OrderResponse> findByOrderNumber(@PathVariable String orderNumber,
                                                         Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        return ApiResponse.ok(orderService.findByOrderNumber(orderNumber, user.getId()));
    }

    @PostMapping("/{orderNumber}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable String orderNumber,
                                              Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        return ApiResponse.ok("Order cancelled", orderService.cancelOrder(orderNumber, user.getId()));
    }
}
