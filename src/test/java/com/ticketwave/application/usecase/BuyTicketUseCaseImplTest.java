package com.ticketwave.application.usecase;

import com.ticketwave.domain.antifraud.model.FraudCheckResult;
import com.ticketwave.application.usecase.CheckOrderFraudUseCase;
import com.ticketwave.domain.common.exception.FraudDetectedException;
import com.ticketwave.domain.common.exception.InsufficientTicketsException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.domain.event.model.EventCategory;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.domain.event.model.TicketType;
import com.ticketwave.application.usecase.FindEventUseCase;
import com.ticketwave.domain.event.repository.TicketTypeRepository;
import com.ticketwave.application.usecase.RedeemPromotionUseCase;
import com.ticketwave.application.usecase.ValidatePromotionUseCase;
import com.ticketwave.application.dto.OrderCreateRequest;
import com.ticketwave.application.dto.OrderItemRequest;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.application.mapper.OrderMapper;
import com.ticketwave.domain.ticket.event.OrderSubmittedEvent;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.domain.ticket.repository.OrderItemRepository;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import com.ticketwave.domain.venue.repository.SeatRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BuyTicketUseCaseImplTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private CheckOrderFraudUseCase checkOrderFraudUseCase;
    @Mock
    private FindEventUseCase findEventUseCase;
    @Mock
    private TicketTypeRepository ticketTypeRepository;
    @Mock
    private SeatRepository seatRepository;
    @Mock
    private ValidatePromotionUseCase validatePromotionUseCase;
    @Mock
    private RedeemPromotionUseCase redeemPromotionUseCase;
    @Mock
    private DomainEventBus eventBus;

    private BuyTicketUseCaseImpl useCase;

    @BeforeEach
    void setUp() {
        useCase = new BuyTicketUseCaseImpl(
                orderRepository, orderItemRepository, checkOrderFraudUseCase, findEventUseCase,
                ticketTypeRepository, seatRepository, validatePromotionUseCase,
                redeemPromotionUseCase, eventBus, new OrderMapper());
    }

    private EventResponse publishedEvent() {
        return new EventResponse(
                1L, "Rock Night", "desc", EventCategory.CONCERT, EventStatus.PUBLISHED, "The Band",
                LocalDateTime.now().plusDays(30),
                LocalDateTime.now().minusDays(1),
                LocalDateTime.now().plusDays(1),
                10L, "Stadium", "City", List.of());
    }

    private TicketType ticketType() {
        TicketType tt = new TicketType();
        tt.setId(1L);
        tt.setName("General");
        tt.setPrice(new BigDecimal("100.00"));
        tt.setTotalQuantity(100);
        tt.setSoldQuantity(0);
        tt.setEventId(1L);
        return tt;
    }

    private OrderCreateRequest request() {
        return new OrderCreateRequest(1L, List.of(new OrderItemRequest(1L, null)), null);
    }

    @Test
    void buy_createsPendingOrderAndPublishesSubmittedEvent() {
        when(checkOrderFraudUseCase.checkOrderFraud(7L)).thenReturn(FraudCheckResult.clean());
        when(findEventUseCase.findById(1L)).thenReturn(publishedEvent());
        when(ticketTypeRepository.findById(1L)).thenReturn(Optional.of(ticketType()));
        when(ticketTypeRepository.incrementSoldQuantity(1L, 1)).thenReturn(1);
        when(orderRepository.save(any(Order.class))).thenAnswer(inv -> {
            Order order = inv.getArgument(0);
            order.setId(10L);
            order.setCreatedAt(LocalDateTime.now());
            return order;
        });

        OrderResponse response = useCase.buy(7L, request());

        assertThat(response.id()).isEqualTo(10L);
        assertThat(response.userId()).isEqualTo(7L);
        assertThat(response.status()).isEqualTo(OrderStatus.PENDING);
        assertThat(response.totalAmount()).isEqualByComparingTo("100.00");
        assertThat(response.items()).hasSize(1);

        ArgumentCaptor<OrderSubmittedEvent> captor = ArgumentCaptor.forClass(OrderSubmittedEvent.class);
        verify(eventBus).publish(captor.capture());
        assertThat(captor.getValue().getOrderId()).isEqualTo(10L);
        assertThat(captor.getValue().getOrderNumber()).isEqualTo(response.orderNumber());
        assertThat(captor.getValue().getUserId()).isEqualTo(7L);
    }

    @Test
    void buy_rejectsWhenFraudDetected() {
        when(checkOrderFraudUseCase.checkOrderFraud(7L))
                .thenReturn(FraudCheckResult.detected("Too many pending orders"));

        assertThatThrownBy(() -> useCase.buy(7L, request()))
                .isInstanceOf(FraudDetectedException.class)
                .hasMessageContaining("Too many pending orders");

        verify(orderRepository, never()).save(any());
        verify(eventBus, never()).publish(any());
    }

    @Test
    void buy_throwsInsufficientTicketsWhenNoInventoryLeft() {
        when(checkOrderFraudUseCase.checkOrderFraud(7L)).thenReturn(FraudCheckResult.clean());
        when(findEventUseCase.findById(1L)).thenReturn(publishedEvent());
        when(ticketTypeRepository.findById(1L)).thenReturn(Optional.of(ticketType()));
        when(ticketTypeRepository.incrementSoldQuantity(1L, 1)).thenReturn(0);

        assertThatThrownBy(() -> useCase.buy(7L, request()))
                .isInstanceOf(InsufficientTicketsException.class);

        verify(orderRepository, never()).save(any());
    }
}
