package com.soe.jcb.eventdriven.demo.ticket.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.ticket.domain.Ticket;
import com.soe.jcb.eventdriven.demo.ticket.domain.TicketRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
public class TicketRepositoryJpaAdapter implements TicketRepository {

    private final TicketJpaRepository jpaRepository;

    public TicketRepositoryJpaAdapter(TicketJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    @Transactional
    public List<Ticket> saveAll(List<Ticket> tickets) {
        return jpaRepository.saveAll(tickets.stream().map(this::toJpa).toList())
                .stream().map(this::toDomain).toList();
    }

    @Override
    public Optional<Ticket> findByTicketCode(String ticketCode) {
        return jpaRepository.findByTicketCode(ticketCode).map(this::toDomain);
    }

    @Override
    public List<Ticket> findByUserId(Long userId) {
        return jpaRepository.findByUserId(userId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Ticket> findByOrderId(Long orderId) {
        return jpaRepository.findByOrderId(orderId).stream().map(this::toDomain).toList();
    }

    private TicketJpaEntity toJpa(Ticket ticket) {
        TicketJpaEntity entity = new TicketJpaEntity();
        entity.setId(ticket.getId());
        entity.setTicketCode(ticket.getTicketCode());
        entity.setOrderItemId(ticket.getOrderItemId());
        entity.setStatus(ticket.getStatus());
        entity.setIssuedAt(ticket.getIssuedAt());
        entity.setValidatedAt(ticket.getValidatedAt());
        return entity;
    }

    private Ticket toDomain(TicketJpaEntity entity) {
        return new Ticket(entity.getId(), entity.getTicketCode(), entity.getOrderItemId(),
                entity.getStatus(), entity.getIssuedAt(), entity.getValidatedAt());
    }
}