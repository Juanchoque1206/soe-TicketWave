package com.soe.jcb.eventdriven.demo.order.service;

import com.soe.jcb.eventdriven.demo.common.event.DomainEventBus;
import com.soe.jcb.eventdriven.demo.order.dto.OrderCreateRequest;
import com.soe.jcb.eventdriven.demo.order.dto.OrderItemRequest;
import com.soe.jcb.eventdriven.demo.order.dto.OrderResponse;
import com.soe.jcb.eventdriven.demo.order.event.OrderCancelledEvent;
import com.soe.jcb.eventdriven.demo.order.event.OrderSubmittedEvent;
import com.soe.jcb.eventdriven.demo.event.entity.Event;
import com.soe.jcb.eventdriven.demo.event.entity.EventStatus;
import com.soe.jcb.eventdriven.demo.order.entity.Order;
import com.soe.jcb.eventdriven.demo.order.entity.OrderItem;
import com.soe.jcb.eventdriven.demo.order.entity.OrderStatus;
import com.soe.jcb.eventdriven.demo.promotion.entity.Promotion;
import com.soe.jcb.eventdriven.demo.venue.entity.Seat;
import com.soe.jcb.eventdriven.demo.event.entity.TicketType;
import com.soe.jcb.eventdriven.demo.user.entity.User;
import com.soe.jcb.eventdriven.demo.common.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.common.exception.DuplicatePurchaseException;
import com.soe.jcb.eventdriven.demo.common.exception.InsufficientTicketsException;
import com.soe.jcb.eventdriven.demo.common.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.event.repository.EventRepository;
import com.soe.jcb.eventdriven.demo.order.repository.OrderItemRepository;
import com.soe.jcb.eventdriven.demo.order.repository.OrderRepository;
import com.soe.jcb.eventdriven.demo.promotion.repository.PromotionRepository;
import com.soe.jcb.eventdriven.demo.promotion.service.PromotionService;
import com.soe.jcb.eventdriven.demo.venue.repository.SeatRepository;
import com.soe.jcb.eventdriven.demo.event.repository.TicketTypeRepository;
import com.soe.jcb.eventdriven.demo.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService {

    private static final int MAX_PENDING_ORDERS_PER_USER = 5;
    private static final int FRAUD_CHECK_MINUTES = 10;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EventRepository eventRepository;
    private final TicketTypeRepository ticketTypeRepository;
    private final UserRepository userRepository;
    private final SeatRepository seatRepository;
    private final PromotionRepository promotionRepository;
    private final PromotionService promotionService;
    private final DomainEventBus eventBus;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        EventRepository eventRepository,
                        TicketTypeRepository ticketTypeRepository,
                        UserRepository userRepository,
                        SeatRepository seatRepository,
                        PromotionRepository promotionRepository,
                        PromotionService promotionService,
                        DomainEventBus eventBus) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.eventRepository = eventRepository;
        this.ticketTypeRepository = ticketTypeRepository;
        this.userRepository = userRepository;
        this.seatRepository = seatRepository;
        this.promotionRepository = promotionRepository;
        this.promotionService = promotionService;
        this.eventBus = eventBus;
    }

    @Transactional
    public OrderResponse createOrder(Long userId, OrderCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        // Fraud check: limit pending orders
        long pendingCount = orderRepository.countByUserIdAndStatusAndCreatedAtAfter(
                userId, OrderStatus.PENDING,
                LocalDateTime.now().minusMinutes(FRAUD_CHECK_MINUTES));
        if (pendingCount >= MAX_PENDING_ORDERS_PER_USER) {
            throw new BusinessRuleException("Too many pending orders. Please complete or cancel existing orders.");
        }

        // Validate event
        Event event = eventRepository.findById(request.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", request.eventId()));

        if (event.getStatus() != EventStatus.PUBLISHED) {
            throw new BusinessRuleException("Event is not available for ticket purchase");
        }

        LocalDateTime now = LocalDateTime.now();
        if (event.getSalesStartDate() != null && now.isBefore(event.getSalesStartDate())) {
            throw new BusinessRuleException("Ticket sales have not started yet");
        }
        if (event.getSalesEndDate() != null && now.isAfter(event.getSalesEndDate())) {
            throw new BusinessRuleException("Ticket sales have ended");
        }

        // Create order
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.items()) {
            TicketType ticketType = ticketTypeRepository.findById(itemReq.ticketTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("TicketType", "id", itemReq.ticketTypeId()));

            if (!ticketType.getEvent().getId().equals(request.eventId())) {
                throw new BusinessRuleException("Ticket type does not belong to the requested event");
            }

            // Atomically reserve ticket
            int updated = ticketTypeRepository.incrementSoldQuantity(ticketType.getId(), 1);
            if (updated == 0) {
                throw new InsufficientTicketsException(ticketType.getId(), 1, ticketType.getAvailableQuantity());
            }

            OrderItem item = new OrderItem();
            item.setOrder(order);
            item.setTicketType(ticketType);
            item.setUnitPrice(ticketType.getPrice());

            // Handle seat assignment
            if (itemReq.seatId() != null) {
                Seat seat = seatRepository.findById(itemReq.seatId())
                        .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", itemReq.seatId()));

                orderItemRepository.findActiveBySeatAndEvent(request.eventId(), seat.getId())
                        .ifPresent(existing -> {
                            throw new DuplicatePurchaseException(
                                    "Seat " + seat.getRow() + seat.getNumber() + " is already reserved");
                        });

                item.setSeat(seat);
            }

            totalAmount = totalAmount.add(ticketType.getPrice());
            order.getItems().add(item);
        }

        // Apply promotion
        if (request.promotionCode() != null && !request.promotionCode().isBlank()) {
            Promotion promotion = promotionRepository
                    .findByCodeAndActiveTrue(request.promotionCode().toUpperCase())
                    .orElseThrow(() -> new BusinessRuleException("Invalid promotion code"));

            BigDecimal discount = promotionService.calculateDiscount(promotion, totalAmount);
            order.setPromotionCode(request.promotionCode().toUpperCase());
            order.setDiscountAmount(discount);
            totalAmount = totalAmount.subtract(discount);

            promotionService.redeem(promotion.getId());
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        eventBus.publish(new OrderSubmittedEvent(
                order.getId(), order.getOrderNumber(), userId));

        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Order cannot be confirmed in status: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CONFIRMED);
        order = orderRepository.save(order);
        return OrderResponse.from(order);
    }

    @Transactional
    public OrderResponse cancelOrder(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessRuleException("Order does not belong to the current user");
        }

        if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
            throw new BusinessRuleException("Order cannot be cancelled in status: " + order.getStatus());
        }

        for (OrderItem item : order.getItems()) {
            ticketTypeRepository.decrementSoldQuantity(item.getTicketType().getId(), 1);
        }

        order.setStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);

        eventBus.publish(new OrderCancelledEvent(
                order.getId(), order.getOrderNumber(), userId));

        return OrderResponse.from(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> findByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream()
                .map(OrderResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findByOrderNumber(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (!order.getUser().getId().equals(userId)) {
            throw new BusinessRuleException("Order does not belong to the current user");
        }

        return OrderResponse.from(order);
    }
}
