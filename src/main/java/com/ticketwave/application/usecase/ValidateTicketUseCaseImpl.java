package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.TicketResponse;
import com.ticketwave.application.mapper.TicketMapper;
import com.ticketwave.domain.ticket.event.TicketValidatedEvent;
import com.ticketwave.domain.ticket.model.Ticket;
import com.ticketwave.domain.ticket.model.TicketStatus;
import com.ticketwave.application.usecase.ValidateTicketUseCase;
import com.ticketwave.domain.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ValidateTicketUseCaseImpl implements ValidateTicketUseCase {

    private final TicketRepository ticketRepository;
    private final DomainEventBus eventBus;
    private final TicketMapper ticketMapper;

    public ValidateTicketUseCaseImpl(TicketRepository ticketRepository,
                                     DomainEventBus eventBus,
                                     TicketMapper ticketMapper) {
        this.ticketRepository = ticketRepository;
        this.eventBus = eventBus;
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional
    public TicketResponse validate(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketCode", ticketCode));

        if (ticket.getStatus() != TicketStatus.VALID) {
            throw new BusinessRuleException("Ticket is not valid. Current status: " + ticket.getStatus());
        }

        ticket.setStatus(TicketStatus.USED);
        ticket.setValidatedAt(LocalDateTime.now());
        ticket = ticketRepository.save(ticket);

        eventBus.publish(new TicketValidatedEvent(
                ticket.getId(), ticket.getTicketCode(), ticket.getOrderId()));

        return ticketMapper.toResponse(ticket);
    }
}
