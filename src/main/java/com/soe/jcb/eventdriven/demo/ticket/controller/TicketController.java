package com.soe.jcb.eventdriven.demo.ticket.controller;

import com.soe.jcb.eventdriven.demo.common.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.ticket.dto.TicketResponse;
import com.soe.jcb.eventdriven.demo.user.entity.User;
import com.soe.jcb.eventdriven.demo.ticket.service.TicketService;
import com.soe.jcb.eventdriven.demo.user.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final UserService userService;

    public TicketController(TicketService ticketService, UserService userService) {
        this.ticketService = ticketService;
        this.userService = userService;
    }

    @GetMapping
    public ApiResponse<List<TicketResponse>> findMyTickets(Principal principal) {
        User user = userService.findEntityByEmail(principal.getName());
        return ApiResponse.ok(ticketService.findByUser(user.getId()));
    }

    @GetMapping("/{ticketCode}")
    public ApiResponse<TicketResponse> findByTicketCode(@PathVariable String ticketCode) {
        return ApiResponse.ok(ticketService.findByTicketCode(ticketCode));
    }

    @PostMapping("/{ticketCode}/validate")
    public ApiResponse<TicketResponse> validateTicket(@PathVariable String ticketCode) {
        return ApiResponse.ok("Ticket validated", ticketService.validateTicket(ticketCode));
    }
}
