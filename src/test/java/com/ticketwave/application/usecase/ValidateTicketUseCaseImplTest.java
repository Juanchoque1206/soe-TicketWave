package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.TicketResponse;
import com.ticketwave.application.mapper.TicketMapper;
import com.ticketwave.domain.ticket.event.TicketValidatedEvent;
import com.ticketwave.domain.ticket.model.Ticket;
import com.ticketwave.domain.ticket.model.TicketStatus;
import com.ticketwave.domain.ticket.repository.TicketRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ValidateTicketUseCaseImplTest {

    @Mock
    private TicketRepository ticketRepository;
    @Mock
    private DomainEventBus eventBus;

    private ValidateTicketUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ValidateTicketUseCaseImpl(ticketRepository, eventBus, new TicketMapper());
    }

    private Ticket validTicket() {
        Ticket ticket = new Ticket();
        ticket.setId(1L);
        ticket.setTicketCode("TICKET-1");
        ticket.setOrderId(5L);
        ticket.setStatus(TicketStatus.VALID);
        ticket.setIssuedAt(LocalDateTime.now());
        return ticket;
    }

    @Test
    void validate_marksTicketAsUsedAndPublishesEvent() {
        Ticket ticket = validTicket();
        when(ticketRepository.findByTicketCode("TICKET-1")).thenReturn(Optional.of(ticket));
        when(ticketRepository.save(ticket)).thenAnswer(inv -> inv.getArgument(0));

        TicketResponse response = useCase.validate("TICKET-1");

        assertThat(response.status()).isEqualTo(TicketStatus.USED);
        assertThat(ticket.getValidatedAt()).isNotNull();

        ArgumentCaptor<TicketValidatedEvent> captor = ArgumentCaptor.forClass(TicketValidatedEvent.class);
        verify(eventBus).publish(captor.capture());
        assertThat(captor.getValue().getTicketId()).isEqualTo(1L);
        assertThat(captor.getValue().getTicketCode()).isEqualTo("TICKET-1");
        assertThat(captor.getValue().getOrderId()).isEqualTo(5L);
    }

    @Test
    void validate_rejectsAlreadyUsedTicket() {
        Ticket ticket = validTicket();
        ticket.setStatus(TicketStatus.USED);
        when(ticketRepository.findByTicketCode("TICKET-1")).thenReturn(Optional.of(ticket));

        assertThatThrownBy(() -> useCase.validate("TICKET-1"))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("not valid");
    }
}
