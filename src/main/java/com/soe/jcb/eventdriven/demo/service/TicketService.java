package com.soe.jcb.eventdriven.demo.service;

import com.soe.jcb.eventdriven.demo.dto.TicketResponse;
import com.soe.jcb.eventdriven.demo.entity.Order;
import com.soe.jcb.eventdriven.demo.entity.OrderItem;
import com.soe.jcb.eventdriven.demo.entity.Ticket;
import com.soe.jcb.eventdriven.demo.entity.TicketStatus;
import com.soe.jcb.eventdriven.demo.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;

    public TicketService(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Transactional
    public List<TicketResponse> issueTickets(Order order) {
        List<Ticket> tickets = new ArrayList<>();

        for (OrderItem item : order.getItems()) {
            Ticket ticket = new Ticket();
            ticket.setTicketCode(UUID.randomUUID().toString());
            ticket.setOrderItem(item);
            ticket.setStatus(TicketStatus.VALID);
            ticket.setIssuedAt(LocalDateTime.now());
            tickets.add(ticket);
        }

        tickets = ticketRepository.saveAll(tickets);
        return tickets.stream().map(TicketResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<TicketResponse> findByUser(Long userId) {
        return ticketRepository.findByUserId(userId).stream()
                .map(TicketResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TicketResponse findByTicketCode(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketCode", ticketCode));
        return TicketResponse.from(ticket);
    }

    @Transactional
    public TicketResponse validateTicket(String ticketCode) {
        Ticket ticket = ticketRepository.findByTicketCode(ticketCode)
                .orElseThrow(() -> new ResourceNotFoundException("Ticket", "ticketCode", ticketCode));

        if (ticket.getStatus() != TicketStatus.VALID) {
            throw new BusinessRuleException("Ticket is not valid. Current status: " + ticket.getStatus());
        }

        ticket.setStatus(TicketStatus.USED);
        ticket.setValidatedAt(LocalDateTime.now());
        ticket = ticketRepository.save(ticket);

        return TicketResponse.from(ticket);
    }
}
