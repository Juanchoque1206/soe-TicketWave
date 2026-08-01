package com.ticketwave.application.usecase;

import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.domain.event.repository.TicketTypeRepository;
import com.ticketwave.application.dto.OrderItemRequest;
import com.ticketwave.application.mapper.OrderMapper;
import com.ticketwave.domain.ticket.event.OrderCancelledEvent;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderItem;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelOrderUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TicketTypeRepository ticketTypeRepository;
    @Mock
    private DomainEventBus eventBus;

    private CancelOrderUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new CancelOrderUseCaseImpl(orderRepository, ticketTypeRepository, eventBus, new OrderMapper());
    }

    private Order pendingOrderWithItem() {
        Order order = new Order();
        order.setId(5L);
        order.setOrderNumber("ORD-1");
        order.setUserId(7L);
        order.setStatus(OrderStatus.PENDING);

        OrderItem item = new OrderItem();
        item.setId(100L);
        item.setTicketTypeId(1L);
        item.setUnitPrice(java.math.BigDecimal.valueOf(50));
        order.addItem(item);

        return order;
    }

    @Test
    void cancel_releasesTicketsAndPublishesCancelledEvent() {
        when(orderRepository.findByOrderNumber("ORD-1")).thenReturn(Optional.of(pendingOrderWithItem()));
        when(ticketTypeRepository.decrementSoldQuantity(1L, 1)).thenReturn(1);
        when(orderRepository.save(org.mockito.ArgumentMatchers.any(Order.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        var response = useCase.cancelOrder("ORD-1", 7L);

        assertThat(response.status()).isEqualTo(OrderStatus.CANCELLED);
        verify(ticketTypeRepository).decrementSoldQuantity(1L, 1);
        ArgumentCaptor<OrderCancelledEvent> captor = ArgumentCaptor.forClass(OrderCancelledEvent.class);
        verify(eventBus).publish(captor.capture());
        assertThat(captor.getValue().getOrderId()).isEqualTo(5L);
        assertThat(captor.getValue().getOrderNumber()).isEqualTo("ORD-1");
        assertThat(captor.getValue().getUserId()).isEqualTo(7L);
    }

    @Test
    void cancel_rejectsOrderOfAnotherUser() {
        when(orderRepository.findByOrderNumber("ORD-1")).thenReturn(Optional.of(pendingOrderWithItem()));

        assertThatThrownBy(() -> useCase.cancelOrder("ORD-1", 99L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("does not belong");
    }
}
