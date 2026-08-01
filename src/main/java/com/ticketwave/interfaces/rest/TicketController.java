package com.ticketwave.interfaces.rest;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.application.dto.TicketResponse;
import com.ticketwave.application.usecase.FindTicketUseCase;
import com.ticketwave.application.usecase.ValidateTicketUseCase;
import com.ticketwave.domain.user.model.User;
import com.ticketwave.application.usecase.FindUserUseCase;
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

    private final FindTicketUseCase findTicketUseCase;
    private final ValidateTicketUseCase validateTicketUseCase;
    private final FindUserUseCase findUserUseCase;

    public TicketController(FindTicketUseCase findTicketUseCase,
                            ValidateTicketUseCase validateTicketUseCase,
                            FindUserUseCase findUserUseCase) {
        this.findTicketUseCase = findTicketUseCase;
        this.validateTicketUseCase = validateTicketUseCase;
        this.findUserUseCase = findUserUseCase;
    }

    @GetMapping
    public ApiResponse<List<TicketResponse>> findMyTickets(Principal principal) {
        User user = findUserUseCase.findEntityByEmail(principal.getName());
        return ApiResponse.ok(findTicketUseCase.findByUser(user.getId()));
    }

    @GetMapping("/{ticketCode}")
    public ApiResponse<TicketResponse> findByTicketCode(@PathVariable String ticketCode) {
        return ApiResponse.ok(findTicketUseCase.findByTicketCode(ticketCode));
    }

    @PostMapping("/{ticketCode}/validate")
    public ApiResponse<TicketResponse> validateTicket(@PathVariable String ticketCode) {
        return ApiResponse.ok("Ticket validated", validateTicketUseCase.validate(ticketCode));
    }
}
