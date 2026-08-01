package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.TicketResponse;

public interface ValidateTicketUseCase {

    TicketResponse validate(String ticketCode);
}
