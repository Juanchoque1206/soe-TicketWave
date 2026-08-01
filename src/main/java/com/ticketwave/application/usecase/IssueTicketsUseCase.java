package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.TicketResponse;

import java.util.List;

public interface IssueTicketsUseCase {

    List<TicketResponse> issue(Long orderId);
}
