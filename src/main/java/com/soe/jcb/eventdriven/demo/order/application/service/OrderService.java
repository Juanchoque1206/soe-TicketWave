package com.soe.jcb.eventdriven.demo.order.application.service;

import com.soe.jcb.eventdriven.demo.common.domain.exception.BusinessRuleException;
import com.soe.jcb.eventdriven.demo.common.domain.exception.DuplicatePurchaseException;
import com.soe.jcb.eventdriven.demo.common.domain.exception.InsufficientTicketsException;
import com.soe.jcb.eventdriven.demo.common.domain.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.order.application.in.OrderUseCase;
import com.soe.jcb.eventdriven.demo.order.application.out.EventOrderPort;
import com.soe.jcb.eventdriven.demo.order.application.out.OrderMessagingPort;
import com.soe.jcb.eventdriven.demo.order.application.out.PromotionOrderPort;
import com.soe.jcb.eventdriven.demo.order.application.out.SeatOrderPort;
import com.soe.jcb.eventdriven.demo.order.application.out.TicketTypeOrderPort;
import com.soe.jcb.eventdriven.demo.order.domain.Order;
import com.soe.jcb.eventdriven.demo.order.domain.OrderItem;
import com.soe.jcb.eventdriven.demo.order.domain.OrderItemRepository;
import com.soe.jcb.eventdriven.demo.order.domain.OrderRepository;
import com.soe.jcb.eventdriven.demo.order.domain.OrderStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class OrderService implements OrderUseCase {

    private static final int MAX_PENDING_ORDERS_PER_USER = 5;
    private static final int FRAUD_CHECK_MINUTES = 10;

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final EventOrderPort eventOrderPort;
    private final TicketTypeOrderPort ticketTypeOrderPort;
    private final SeatOrderPort seatOrderPort;
    private final PromotionOrderPort promotionOrderPort;
    private final OrderMessagingPort orderMessagingPort;

    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        EventOrderPort eventOrderPort,
                        TicketTypeOrderPort ticketTypeOrderPort,
                        SeatOrderPort seatOrderPort,
                        PromotionOrderPort promotionOrderPort,
                        OrderMessagingPort orderMessagingPort) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.eventOrderPort = eventOrderPort;
        this.ticketTypeOrderPort = ticketTypeOrderPort;
        this.seatOrderPort = seatOrderPort;
        this.promotionOrderPort = promotionOrderPort;
        this.orderMessagingPort = orderMessagingPort;
    }

    @Override
    @Transactional
    public OrderResult create(CreateOrderCommand command) {
        long pendingCount = orderRepository.countPendingSince(
                command.userId(), LocalDateTime.now().minusMinutes(FRAUD_CHECK_MINUTES));
        if (pendingCount >= MAX_PENDING_ORDERS_PER_USER) {
            throw new BusinessRuleException("Too many pending orders. Please complete or cancel existing orders.");
        }

        EventOrderPort.EventSnapshot event = eventOrderPort.findById(command.eventId())
                .orElseThrow(() -> new ResourceNotFoundException("Event", "id", command.eventId()));
        validateEventAvailability(event);

        Order order = new Order(null, UUID.randomUUID().toString(), command.userId(),
                OrderStatus.PENDING, BigDecimal.ZERO, null, BigDecimal.ZERO, LocalDateTime.now());

        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CreateOrderItemCommand itemCmd : command.items()) {
            TicketTypeOrderPort.TicketTypeSnapshot ticketType = ticketTypeOrderPort.findById(itemCmd.ticketTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("TicketType", "id", itemCmd.ticketTypeId()));

            if (!ticketType.eventId().equals(command.eventId())) {
                throw new BusinessRuleException("Ticket type does not belong to the requested event");
            }
            if (!ticketTypeOrderPort.reserve(ticketType.id(), 1)) {
                throw new InsufficientTicketsException(ticketType.id(), 1, 0);
            }

            String seatRow = null;
            Integer seatNumber = null;
            if (itemCmd.seatId() != null) {
                SeatOrderPort.SeatSnapshot seat = seatOrderPort.findById(itemCmd.seatId())
                        .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", itemCmd.seatId()));
                if (orderItemRepository.isSeatReserved(command.eventId(), seat.id())) {
                    throw new DuplicatePurchaseException(
                            "Seat " + seat.row() + seat.number() + " is already reserved");
                }
                seatRow = seat.row();
                seatNumber = seat.number();
            }

            totalAmount = totalAmount.add(ticketType.price());
            order.addItem(new OrderItem(null, ticketType.id(), ticketType.name(),
                    ticketType.price(), itemCmd.seatId(), seatRow, seatNumber));
        }

        if (command.promotionCode() != null && !command.promotionCode().isBlank()) {
            PromotionOrderPort.PromotionSnapshot promotion = promotionOrderPort.findByCodeAndActive(
                    command.promotionCode().toUpperCase())
                    .orElseThrow(() -> new BusinessRuleException("Invalid promotion code"));
            BigDecimal discount = calculateDiscount(promotion, totalAmount);
            order.setPromotionCode(command.promotionCode().toUpperCase());
            order.setDiscountAmount(discount);
            totalAmount = totalAmount.subtract(discount);
            promotionOrderPort.incrementUses(promotion.id());
        }

        order.setTotalAmount(totalAmount);
        Order saved = orderRepository.save(order);
        orderMessagingPort.publishOrderCreated(saved.getId(), saved.getOrderNumber(), saved.getUserId());
        return toResult(saved);
    }

    private void validateEventAvailability(EventOrderPort.EventSnapshot event) {
        if (!"PUBLISHED".equals(event.status())) {
            throw new BusinessRuleException("Event is not available for ticket purchase");
        }
        LocalDateTime now = LocalDateTime.now();
        if (event.salesStartDate() != null && now.isBefore(event.salesStartDate())) {
            throw new BusinessRuleException("Ticket sales have not started yet");
        }
        if (event.salesEndDate() != null && now.isAfter(event.salesEndDate())) {
            throw new BusinessRuleException("Ticket sales have ended");
        }
    }

    @Override
    @Transactional
    public OrderResult confirm(Long orderId) {
        Order order = getOrder(orderId);
        order.confirm();
        Order saved = orderRepository.save(order);
        orderMessagingPort.publishOrderConfirmed(saved.getId(), saved.getOrderNumber(), saved.getUserId());
        return toResult(saved);
    }

    @Override
    @Transactional
    public OrderResult cancel(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));

        if (!order.getUserId().equals(userId)) {
            throw new BusinessRuleException("Order does not belong to the current user");
        }

        order.cancel();
        for (OrderItem item : order.getItems()) {
            ticketTypeOrderPort.release(item.getTicketTypeId(), 1);
        }

        Order saved = orderRepository.save(order);
        orderMessagingPort.publishOrderCancelled(saved.getId(), saved.getOrderNumber(), saved.getUserId());
        return toResult(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResult findByOrderNumber(String orderNumber, Long userId) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
        if (!order.getUserId().equals(userId)) {
            throw new BusinessRuleException("Order does not belong to the current user");
        }
        return toResult(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResult findByOrderNumber(String orderNumber) {
        Order order = orderRepository.findByOrderNumber(orderNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "orderNumber", orderNumber));
        return toResult(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResult> findByUser(Long userId) {
        return orderRepository.findByUserIdOrderByCreatedAtDesc(userId).stream().map(this::toResult).toList();
    }

    private Order getOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", orderId));
    }

    private BigDecimal calculateDiscount(PromotionOrderPort.PromotionSnapshot promotion, BigDecimal totalAmount) {
        if (promotion.discountPercentage() != null) {
            return totalAmount.multiply(promotion.discountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, java.math.RoundingMode.HALF_UP);
        } else if (promotion.discountFlat() != null) {
            return promotion.discountFlat().min(totalAmount);
        }
        return BigDecimal.ZERO;
    }

    private OrderResult toResult(Order order) {
        List<OrderItemResult> items = order.getItems().stream()
                .map(i -> new OrderItemResult(i.getId(), i.getTicketTypeName(), i.getUnitPrice(),
                        i.getSeatId(), i.getSeatRow(), i.getSeatNumber()))
                .toList();
        return new OrderResult(order.getId(), order.getOrderNumber(), order.getStatus().name(),
                order.getTotalAmount(), order.getPromotionCode(), order.getDiscountAmount(),
                order.getCreatedAt(), items);
    }
}