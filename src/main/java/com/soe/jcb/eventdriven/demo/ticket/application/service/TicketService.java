package com.soe.jcb.eventdriven.demo.ticket.application.service;

import com.soe.jcb.eventdriven.demo.common.domain.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.common.domain.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.ticket.application.in.TicketUseCase;
import com.soe.jcb.eventdriven.demo.ticket.application.out.TicketOrderQueryPort;
import com.soe.jcb.eventdriven.demo.ticket.domain.Ticket;
import com.soe.jcb.eventdriven.demo.ticket.domain.TicketRepository;
import com.soe.jcb.eventdriven.demo.ticket.domain.TicketStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketService implements TicketUseCase {

    private final TicketRepository ticketRepository;
    private final TicketOrderQueryPort ticketOrderQueryPort;

    public TicketService(TicketRepository ticketRepository, TicketOrderQueryPort ticketOrderQueryPort) {
        this.ticketRepository = ticketRepository;
        this.ticketOrderQueryPort = ticketOrderQueryPort;
    }

    @Override
    @Transactional
    public List<TicketResult> issueTickets(Long orderId) {
        List<TicketOrderQueryPort.OrderItemSnapshot> items = ticketOrderQueryPort.findOrderItemsByOrderId(orderId);

        List<Ticket> tickets = new ArrayList<>();
        for (TicketOrderQueryPort.OrderItemSnapshot item : items) {
            Ticket ticket = new Ticket(null, UUID.randomUUID().toString(), item.id(),
                    TicketStatus.VALID, LocalDateTime.now(), null);
            tickets.add(ticket);
        }
        tickets = ticketRepository.saveAll(tickets);

        Map<Long, TicketOrderQueryPort.OrderItemSnapshot> itemById =
                items.stream().collect(Collectors.toMap(TicketOrderQueryPort.OrderItemSnapshot::id, i -> i));
        return tickets.stream().map(t -> toResult(t, itemById.get(t.getOrderItemId()))).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TicketResult> findByUser(Long userId) {
        return ticketRepository.findByUserId(userId).stream().map(this::toResult).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public TicketResult findByTicketCode(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketCode", ticketCode));
        return toResult(ticket);
    }

    @Override
    @Transactional
    public TicketResult validateTicket(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketCode", ticketCode));
        try {
            ticket.validate(LocalDateTime.now());
        } catch (IllegalStateException ex) {
            throw new BusinessRuleException(ex.getMessage());
        }
        return toResult(ticket);
    }

    private TicketResult toResult(Ticket ticket) {
        return toResult(ticket, null);
    }

    private TicketResult toResult(Ticket ticket, TicketOrderQueryPort.OrderItemSnapshot item) {
        return new TicketResult(ticket.getId(), ticket.getTicketCode(), ticket.getStatus(),
                ticket.getIssuedAt(), ticket.getValidatedAt(),
                item != null ? item.eventName() : null,
                item != null ? item.eventDate() : null,
                item != null ? item.venueName() : null,
                item != null ? item.ticketTypeName() : null,
                item != null ? item.seatRow() : null,
                item != null ? item.seatNumber() : null);
    }
}