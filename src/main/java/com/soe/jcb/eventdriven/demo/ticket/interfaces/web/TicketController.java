package com.soe.jcb.eventdriven.demo.ticket.interfaces.web;

import com.soe.jcb.eventdriven.demo.common.interfaces.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.ticket.application.in.TicketUseCase;
import com.soe.jcb.eventdriven.demo.ticket.interfaces.dto.TicketResponse;
import com.soe.jcb.eventdriven.demo.user.application.in.UserQueryPort;
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

    private final TicketUseCase ticketUseCase;
    private final UserQueryPort userQueryPort;

    public TicketController(TicketUseCase ticketUseCase, UserQueryPort userQueryPort) {
        this.ticketUseCase = ticketUseCase;
        this.userQueryPort = userQueryPort;
    }

    @GetMapping
    public ApiResponse<List<TicketResponse>> findMyTickets(Principal principal) {
        Long userId = userQueryPort.findByEmail(principal.getName()).orElseThrow().getId();
        return ApiResponse.ok(ticketUseCase.findByUser(userId).stream().map(TicketResponse::from).toList());
    }

    @GetMapping("/{ticketCode}")
    public ApiResponse<TicketResponse> findByTicketCode(@PathVariable String ticketCode) {
        return ApiResponse.ok(TicketResponse.from(ticketUseCase.findByTicketCode(ticketCode)));
    }

    @PostMapping("/{ticketCode}/validate")
    public ApiResponse<TicketResponse> validateTicket(@PathVariable String ticketCode) {
        return ApiResponse.ok("Ticket validated", TicketResponse.from(ticketUseCase.validateTicket(ticketCode)));
    }
}