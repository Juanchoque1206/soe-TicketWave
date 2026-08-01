package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.domain.ticket.model.Ticket;
import com.ticketwave.domain.ticket.model.TicketStatus;
import com.ticketwave.application.usecase.RefundTicketsUseCase;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import com.ticketwave.domain.ticket.repository.TicketRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RefundTicketsUseCaseImpl implements RefundTicketsUseCase {

    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    public RefundTicketsUseCaseImpl(OrderRepository orderRepository,
                                    TicketRepository ticketRepository) {
        this.orderRepository = orderRepository;
        this.ticketRepository = ticketRepository;
    }

    @Override
    @Transactional
    public void refund(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() == OrderStatus.REFUNDED) {
            return;
        }

        List<Ticket> tickets = ticketRepository.findByOrderId(orderId);
        for (Ticket ticket : tickets) {
            ticket.setStatus(TicketStatus.REFUNDED);
            ticketRepository.save(ticket);
        }

        order.setStatus(OrderStatus.REFUNDED);
        orderRepository.save(order);
    }
}
