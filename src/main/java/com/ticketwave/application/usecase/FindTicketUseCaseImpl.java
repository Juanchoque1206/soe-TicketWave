package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.application.dto.TicketResponse;
import com.ticketwave.application.mapper.TicketMapper;
import com.ticketwave.domain.ticket.model.Ticket;
import com.ticketwave.application.usecase.FindTicketUseCase;
import com.ticketwave.domain.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class FindTicketUseCaseImpl implements FindTicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;

    public FindTicketUseCaseImpl(TicketRepository ticketRepository, TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResponse> findByUser(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(ticketMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResponse findByTicketCode(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketCode", ticketCode));
        return ticketMapper.toResponse(ticket);
    }
}
