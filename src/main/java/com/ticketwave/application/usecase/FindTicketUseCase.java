package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.TicketResponse;

import java.util.List;

public interface FindTicketUseCase {

    List<TicketResponse> findByUser(Long userId);

    TicketResponse findByTicketCode(String ticketCode);
}
