package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.application.mapper.OrderMapper;
import com.ticketwave.domain.ticket.event.OrderConfirmedEvent;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConfirmOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private DomainEventBus eventBus;

    private ConfirmOrderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new ConfirmOrderUseCaseImpl(orderRepository, eventBus, new OrderMapper());
    }

    private Order pendingOrder() {
        Order order = new Order();
        order.setId(5L);
        order.setOrderNumber("ORD-1");
        order.setUserId(7L);
        order.setStatus(OrderStatus.PENDING);
        return order;
    }

    @Test
    void confirm_confirmsPendingOrderAndPublishesEvent() {
        when(orderRepository.findById(5L)).thenReturn(Optional.of(pendingOrder()));
        when(orderRepository.save(org.mockito.ArgumentMatchers.any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        OrderResponse response = useCase.confirm(5L);

        assertThat(response.status()).isEqualTo(OrderStatus.CONFIRMED);
        ArgumentCaptor<OrderConfirmedEvent> captor = ArgumentCaptor.forClass(OrderConfirmedEvent.class);
        verify(eventBus).publish(captor.capture());
        assertThat(captor.getValue().getOrderId()).isEqualTo(5L);
        assertThat(captor.getValue().getOrderNumber()).isEqualTo("ORD-1");
        assertThat(captor.getValue().getUserId()).isEqualTo(7L);
    }

    @Test
    void confirm_rejectsOrderNotPending() {
        Order order = pendingOrder();
        order.setStatus(OrderStatus.CANCELLED);
        when(orderRepository.findById(5L)).thenReturn(Optional.of(order));

        assertThatThrownBy(() -> useCase.confirm(5L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("cannot be confirmed");

        verify(eventBus, never()).publish(org.mockito.ArgumentMatchers.any());
    }
}
