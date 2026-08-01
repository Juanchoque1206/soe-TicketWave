package com.ticketwave.infrastructure.repository.jpa;

import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.domain.ticket.model.Ticket;
import com.ticketwave.domain.ticket.repository.TicketRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class TicketPersistenceAdapter implements TicketRepository {

    private final JpaTicketRepository jpaTicketRepository;
    private final TicketPersistenceMapper mapper;

    public TicketPersistenceAdapter(JpaTicketRepository jpaTicketRepository, TicketPersistenceMapper mapper) {
        this.jpaTicketRepository = jpaTicketRepository;
        this.mapper = mapper;
    }

    @Override
    public Ticket save(Ticket ticket) {
        return mapper.toDomain(jpaTicketRepository.save(toJpa(ticket)));
    }

    @Override
    public List<Ticket> saveAll(List<Ticket> tickets) {
        List<JpaTicket> saved = jpaTicketRepository.saveAll(tickets.stream().map(this::toJpa).toList());
        return saved.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<Ticket> findByTicketCode(String ticketCode) {
        return jpaTicketRepository.findByTicketCode(ticketCode).map(mapper::toDomain);
    }

    @Override
    public List<Ticket> findByOrderId(Long orderId) {
        return jpaTicketRepository.findByOrderId(orderId).stream().map(mapper::toDomain).toList();
    }

    @Override
    public List<Ticket> findByUserId(Long userId) {
        return jpaTicketRepository.findByUserId(userId).stream().map(mapper::toDomain).toList();
    }

    private JpaTicket toJpa(Ticket ticket) {
        JpaTicket jpa;
        if (ticket.getId() != null) {
            jpa = jpaTicketRepository.findById(ticket.getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Ticket", "id", ticket.getId()));
        } else {
            jpa = new JpaTicket();
        }
        jpa.setTicketCode(ticket.getTicketCode());
        jpa.setOrderId(ticket.getOrderId());
        jpa.setOrderItemId(ticket.getOrderItemId());
        jpa.setStatus(ticket.getStatus());
        jpa.setIssuedAt(ticket.getIssuedAt());
        jpa.setValidatedAt(ticket.getValidatedAt());
        jpa.setEventName(ticket.getEventName());
        jpa.setEventDate(ticket.getEventDate());
        jpa.setVenueName(ticket.getVenueName());
        jpa.setTicketTypeName(ticket.getTicketTypeName());
        jpa.setSeatRow(ticket.getSeatRow());
        jpa.setSeatNumber(ticket.getSeatNumber());
        return jpa;
    }
}
