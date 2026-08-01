package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.TicketResponse;
import com.ticketwave.application.mapper.TicketMapper;
import com.ticketwave.domain.ticket.event.TicketsIssuedEvent;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderItem;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.domain.ticket.model.Ticket;
import com.ticketwave.domain.ticket.model.TicketStatus;
import com.ticketwave.application.usecase.IssueTicketsUseCase;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import com.ticketwave.domain.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class IssueTicketsUseCaseImpl implements IssueTicketsUseCase {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;
    private final DomainEventBus eventBus;
    private final TicketMapper ticketMapper;

    public IssueTicketsUseCaseImpl(OrderRepository orderRepository,
                                   TicketRepository ticketRepository,
                                   DomainEventBus eventBus,
                                   TicketMapper ticketMapper) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
        this.eventBus = eventBus;
        this.ticketMapper = ticketMapper;
    }

    @Override
    @Transactional
    public List<TicketResponse> issue(Long orderId) {
        List<Ticket> existing = ticketRepository.findByOrderId(orderId);
        if (!existing.isEmpty()) {
            return existing.stream().map(ticketMapper::toResponse).toList();
        }

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessRuleException("Tickets can only be issued for confirmed orders");
        }

        List<Ticket> tickets = new ArrayList<>();
        for (OrderItem item : order.getItems()) {
            Ticket ticket = new Ticket();
            ticket.setTicketCode(UUID.randomUUID().toString());
            ticket.setOrderId(order.getId());
            ticket.setOrderItemId(item.getId());
            ticket.setStatus(TicketStatus.VALID);
            ticket.setIssuedAt(LocalDateTime.now());
            ticket.setEventName(order.getEventName());
            ticket.setEventDate(order.getEventDate());
            ticket.setVenueName(order.getVenueName());
            ticket.setTicketTypeName(item.getTicketTypeName());
            ticket.setSeatRow(item.getSeatRow());
            ticket.setSeatNumber(item.getSeatNumber());
            tickets.add(ticket);
        }

        tickets = ticketRepository.saveAll(tickets);

        List<Long> ticketIds = tickets.stream().map(Ticket::getId).toList();
        eventBus.publish(new TicketsIssuedEvent(
                order.getId(), order.getOrderNumber(), order.getUserId(), ticketIds));

        return tickets.stream().map(ticketMapper::toResponse).toList();
    }
}
