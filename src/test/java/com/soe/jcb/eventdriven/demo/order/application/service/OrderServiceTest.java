package com.soe.jcb.eventdriven.demo.order.application.service;

import com.soe.jcb.eventdriven.demo.common.domain.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.order.application.in.OrderUseCase;
import com.soe.jcb.eventdriven.demo.order.application.out.EventOrderPort;
import com.soe.jcb.eventdriven.demo.order.application.out.OrderMessagingPort;
import com.soe.jcb.eventdriven.demo.order.application.out.PromotionOrderPort;
import com.soe.jcb.eventdriven.demo.order.application.out.SeatOrderPort;
import com.soe.jcb.eventdriven.demo.order.application.out.TicketTypeOrderPort;
import com.soe.jcb.eventdriven.demo.order.domain.Order;
import com.soe.jcb.eventdriven.demo.order.domain.OrderItemRepository;
import com.soe.jcb.eventdriven.demo.order.domain.OrderRepository;
import com.soe.jcb.eventdriven.demo.order.domain.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderServiceTest {

    private InMemoryOrderRepository orderRepository;
    private OrderService service;

    @BeforeEach
    void setUp() {
        orderRepository = new InMemoryOrderRepository();
        service = new OrderService(orderRepository, (eventId, seatId) -> false,
                eventPort("PUBLISHED"), ticketTypePort(), seatPort(), promotionPort(), messagingPort());
    }

    @Test
    void createOrderReservesTicketsAndTotals() {
        OrderUseCase.OrderResult result = service.create(command(1L, null));

        assertEquals(OrderStatus.PENDING.name(), result.status());
        assertEquals(0, new BigDecimal("50.00").compareTo(result.totalAmount()));
        assertEquals(1, result.items().size());
    }

    @Test
    void createOrderAppliesPromotion() {
        OrderUseCase.CreateOrderCommand command = new OrderUseCase.CreateOrderCommand(
                42L, 1L, "SAVE10",
                List.of(new OrderUseCase.CreateOrderItemCommand(1L, null)));

        OrderUseCase.OrderResult result = service.create(command);

        assertEquals("SAVE10", result.promotionCode());
        assertEquals(0, new BigDecimal("45.00").compareTo(result.totalAmount()));
    }

    @Test
    void createOrderRejectsUnpublishedEvent() {
        OrderService unpublished = new OrderService(new InMemoryOrderRepository(),
                (eventId, seatId) -> false, eventPort("CANCELLED"),
                ticketTypePort(), seatPort(), promotionPort(), messagingPort());

        assertThrows(BusinessRuleException.class, () -> unpublished.create(command(1L, null)));
    }

    @Test
    void confirmOrderTransitionsToConfirmed() {
        OrderUseCase.OrderResult created = service.create(command(1L, null));
        OrderUseCase.OrderResult confirmed = service.confirm(created.id());
        assertEquals(OrderStatus.CONFIRMED.name(), confirmed.status());
    }

    @Test
    void cancelOrderTransitionsToCancelled() {
        OrderUseCase.OrderResult created = service.create(command(1L, null));
        OrderUseCase.OrderResult cancelled = service.cancel(created.orderNumber(), 42L);
        assertEquals(OrderStatus.CANCELLED.name(), cancelled.status());
    }

    private OrderUseCase.CreateOrderCommand command(Long ticketTypeId, Long seatId) {
        return new OrderUseCase.CreateOrderCommand(42L, 1L, null,
                List.of(new OrderUseCase.CreateOrderItemCommand(ticketTypeId, seatId)));
    }

    private EventOrderPort eventPort(String status) {
        return event -> Optional.of(new EventOrderPort.EventSnapshot(
                event, status, LocalDateTime.now().minusDays(1), LocalDateTime.now().plusDays(1)));
    }

    private TicketTypeOrderPort ticketTypePort() {
        return new TicketTypeOrderPort() {
            @Override
            public Optional<TicketTypeSnapshot> findById(Long id) {
                return Optional.of(new TicketTypeSnapshot(id, 1L, "GA", BigDecimal.valueOf(50)));
            }

            @Override
            public boolean belongsToEvent(Long ticketTypeId, Long eventId) {
                return true;
            }

            @Override
            public boolean reserve(Long id, int qty) {
                return true;
            }

            @Override
            public boolean release(Long id, int qty) {
                return true;
            }
        };
    }

    private SeatOrderPort seatPort() {
        return new SeatOrderPort() {
            @Override
            public Optional<SeatSnapshot> findById(Long id) {
                return Optional.of(new SeatSnapshot(id, "A", 1));
            }

            @Override
            public boolean existsById(Long id) {
                return true;
            }
        };
    }

    private PromotionOrderPort promotionPort() {
        return new PromotionOrderPort() {
            @Override
            public Optional<PromotionSnapshot> findByCodeAndActive(String code) {
                return Optional.of(new PromotionSnapshot(5L, "SAVE10", new BigDecimal("10"), null, null));
            }

            @Override
            public boolean incrementUses(Long id) {
                return true;
            }
        };
    }

    private OrderMessagingPort messagingPort() {
        return new OrderMessagingPort() {
            @Override
            public void publishOrderCreated(Long orderId, String orderNumber, Long userId) {
            }

            @Override
            public void publishOrderConfirmed(Long orderId, String orderNumber, Long userId) {
            }

            @Override
            public void publishOrderCancelled(Long orderId, String orderNumber, Long userId) {
            }
        };
    }

    private static class InMemoryOrderRepository implements OrderRepository {
        private final List<Order> store = new ArrayList<>();
        private long nextId = 1;

        @Override
        public Order save(Order order) {
            if (order.getId() == null) {
                order.setId(nextId++);
                store.add(order);
            } else {
                store.removeIf(o -> o.getId().equals(order.getId()));
                store.add(order);
            }
            return order;
        }

        @Override
        public Optional<Order> findById(Long id) {
            return store.stream().filter(o -> o.getId().equals(id)).findFirst();
        }

        @Override
        public Optional<Order> findByOrderNumber(String orderNumber) {
            return store.stream().filter(o -> o.getOrderNumber().equals(orderNumber)).findFirst();
        }

        @Override
        public List<Order> findByUserIdOrderByCreatedAtDesc(Long userId) {
            return store.stream().filter(o -> o.getUserId().equals(userId)).toList();
        }

        @Override
        public long countPendingSince(Long userId, LocalDateTime after) {
            return 0;
        }
    }
}