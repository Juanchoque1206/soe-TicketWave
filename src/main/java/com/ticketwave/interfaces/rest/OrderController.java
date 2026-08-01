package com.ticketwave.interfaces.rest;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.application.dto.OrderCreateRequest;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.application.usecase.BuyTicketUseCase;
import com.ticketwave.application.usecase.CancelOrderUseCase;
import com.ticketwave.application.usecase.FindOrderUseCase;
import com.ticketwave.domain.user.model.User;
import com.ticketwave.application.usecase.FindUserUseCase;
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

    private final BuyTicketUseCase buyTicketUseCase;
    private final FindOrderUseCase findOrderUseCase;
    private final CancelOrderUseCase cancelOrderUseCase;
    private final FindUserUseCase findUserUseCase;

    public OrderController(BuyTicketUseCase buyTicketUseCase,
                           FindOrderUseCase findOrderUseCase,
                           CancelOrderUseCase cancelOrderUseCase,
                           FindUserUseCase findUserUseCase) {
        this.buyTicketUseCase = buyTicketUseCase;
        this.findOrderUseCase = findOrderUseCase;
        this.cancelOrderUseCase = cancelOrderUseCase;
        this.findUserUseCase = findUserUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<OrderResponse> create(@Valid @RequestBody OrderCreateRequest request,
                                              Principal principal) {
        User user = findUserUseCase.findEntityByEmail(principal.getName());
        OrderResponse order = buyTicketUseCase.buy(user.getId(), request);
        return ApiResponse.ok("Order created successfully", order);
    }

    @GetMapping
    public ApiResponse<List<OrderResponse>> findMyOrders(Principal principal) {
        User user = findUserUseCase.findEntityByEmail(principal.getName());
        return ApiResponse.ok(findOrderUseCase.findByUser(user.getId()));
    }

    @GetMapping("/{orderNumber}")
    public ApiResponse<OrderResponse> findByOrderNumber(@PathVariable String orderNumber,
                                                         Principal principal) {
        User user = findUserUseCase.findEntityByEmail(principal.getName());
        return ApiResponse.ok(findOrderUseCase.findByOrderNumber(orderNumber, user.getId()));
    }

    @PostMapping("/{orderNumber}/cancel")
    public ApiResponse<OrderResponse> cancel(@PathVariable String orderNumber,
                                              Principal principal) {
        User user = findUserUseCase.findEntityByEmail(principal.getName());
        return ApiResponse.ok("Order cancelled", cancelOrderUseCase.cancelOrder(orderNumber, user.getId()));
    }
}
