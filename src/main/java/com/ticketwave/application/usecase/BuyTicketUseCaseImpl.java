package com.ticketwave.application.usecase;

import com.ticketwave.domain.antifraud.model.FraudCheckResult;
import com.ticketwave.application.usecase.CheckOrderFraudUseCase;
import com.ticketwave.domain.common.exception.BusinessRuleException;
import com.ticketwave.domain.common.exception.DuplicatePurchaseException;
import com.ticketwave.domain.common.exception.FraudDetectedException;
import com.ticketwave.domain.common.exception.InsufficientTicketsException;
import com.ticketwave.domain.common.exception.ResourceNotFoundException;
import com.ticketwave.infrastructure.eventbus.DomainEventBus;
import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.domain.event.model.EventStatus;
import com.ticketwave.domain.event.model.TicketType;
import com.ticketwave.application.usecase.FindEventUseCase;
import com.ticketwave.domain.event.repository.TicketTypeRepository;
import com.ticketwave.application.dto.PromotionResponse;
import com.ticketwave.application.usecase.RedeemPromotionUseCase;
import com.ticketwave.application.usecase.ValidatePromotionUseCase;
import com.ticketwave.application.dto.OrderCreateRequest;
import com.ticketwave.application.dto.OrderItemRequest;
import com.ticketwave.application.dto.OrderResponse;
import com.ticketwave.application.mapper.OrderMapper;
import com.ticketwave.domain.ticket.event.OrderSubmittedEvent;
import com.ticketwave.domain.ticket.model.Order;
import com.ticketwave.domain.ticket.model.OrderItem;
import com.ticketwave.domain.ticket.model.OrderStatus;
import com.ticketwave.application.usecase.BuyTicketUseCase;
import com.ticketwave.domain.ticket.repository.OrderItemRepository;
import com.ticketwave.domain.ticket.repository.OrderRepository;
import com.ticketwave.domain.venue.model.Seat;
import com.ticketwave.domain.venue.repository.SeatRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class BuyTicketUseCaseImpl implements BuyTicketUseCase {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CheckOrderFraudUseCase checkOrderFraudUseCase;
    private final FindEventUseCase findEventUseCase;
    private final TicketTypeRepository ticketTypeRepository;
    private final SeatRepository seatRepository;
    private final ValidatePromotionUseCase validatePromotionUseCase;
    private final RedeemPromotionUseCase redeemPromotionUseCase;
    private final DomainEventBus eventBus;
    private final OrderMapper orderMapper;

    public BuyTicketUseCaseImpl(OrderRepository orderRepository,
                                OrderItemRepository orderItemRepository,
                                CheckOrderFraudUseCase checkOrderFraudUseCase,
                                FindEventUseCase findEventUseCase,
                                TicketTypeRepository ticketTypeRepository,
                                SeatRepository seatRepository,
                                ValidatePromotionUseCase validatePromotionUseCase,
                                RedeemPromotionUseCase redeemPromotionUseCase,
                                DomainEventBus eventBus,
                                OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.checkOrderFraudUseCase = checkOrderFraudUseCase;
        this.findEventUseCase = findEventUseCase;
        this.ticketTypeRepository = ticketTypeRepository;
        this.seatRepository = seatRepository;
        this.validatePromotionUseCase = validatePromotionUseCase;
        this.redeemPromotionUseCase = redeemPromotionUseCase;
        this.eventBus = eventBus;
        this.orderMapper = orderMapper;
    }

    @Override
    @Transactional
    public OrderResponse buy(Long userId, OrderCreateRequest request) {
        FraudCheckResult fraudCheck = checkOrderFraudUseCase.checkOrderFraud(userId);
        if (fraudCheck.fraudDetected()) {
            throw new FraudDetectedException(fraudCheck.reason());
        }

        EventResponse event = findEventUseCase.findById(request.eventId());

        if (event.status() != EventStatus.PUBLISHED) {
            throw new BusinessRuleException("Event is not available for ticket purchase");
        }

        LocalDateTime now = LocalDateTime.now();
        if (event.salesStartDate() != null && now.isBefore(event.salesStartDate())) {
            throw new BusinessRuleException("Ticket sales have not started yet");
        }
        if (event.salesEndDate() != null && now.isAfter(event.salesEndDate())) {
            throw new BusinessRuleException("Ticket sales have ended");
        }

        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setStatus(OrderStatus.PENDING);
        order.setEventId(event.id());
        order.setEventName(event.name());
        order.setEventDate(event.eventDate());
        order.setVenueName(event.venueName());

        BigDecimal totalAmount = BigDecimal.ZERO;

        for (OrderItemRequest itemReq : request.items()) {
            TicketType ticketType = ticketTypeRepository.findById(itemReq.ticketTypeId())
                    .orElseThrow(() -> new ResourceNotFoundException("TicketType", "id", itemReq.ticketTypeId()));

            if (!ticketType.getEventId().equals(request.eventId())) {
                throw new BusinessRuleException("Ticket type does not belong to the requested event");
            }

            int updated = ticketTypeRepository.incrementSoldQuantity(ticketType.getId(), 1);
            if (updated == 0) {
                throw new InsufficientTicketsException(ticketType.getId(), 1, ticketType.getAvailableQuantity());
            }

            OrderItem item = new OrderItem();
            item.setTicketTypeId(ticketType.getId());
            item.setTicketTypeName(ticketType.getName());
            item.setUnitPrice(ticketType.getPrice());

            if (itemReq.seatId() != null) {
                Seat seat = seatRepository.findById(itemReq.seatId())
                        .orElseThrow(() -> new ResourceNotFoundException("Seat", "id", itemReq.seatId()));

                orderItemRepository.findActiveBySeatAndEvent(request.eventId(), seat.getId())
                        .ifPresent(existing -> {
                            throw new DuplicatePurchaseException(
                                    "Seat " + seat.getRow() + seat.getNumber() + " is already reserved");
                        });

                item.setSeatId(seat.getId());
                item.setSeatRow(seat.getRow());
                item.setSeatNumber(seat.getNumber());
            }

            totalAmount = totalAmount.add(ticketType.getPrice());
            order.addItem(item);
        }

        if (request.promotionCode() != null && !request.promotionCode().isBlank()) {
            PromotionResponse promotion = validatePromotionUseCase.validate(request.promotionCode(), event.venueId());

            BigDecimal discount = calculateDiscount(promotion, totalAmount);
            order.setPromotionCode(request.promotionCode().toUpperCase());
            order.setDiscountAmount(discount);
            totalAmount = totalAmount.subtract(discount);

            redeemPromotionUseCase.redeem(promotion.id());
        }

        order.setTotalAmount(totalAmount);
        order = orderRepository.save(order);

        eventBus.publish(new OrderSubmittedEvent(
                order.getId(), order.getOrderNumber(), userId));

        return orderMapper.toResponse(order);
    }

    private BigDecimal calculateDiscount(PromotionResponse promotion, BigDecimal totalAmount) {
        if (promotion.discountPercentage() != null) {
            return totalAmount.multiply(promotion.discountPercentage())
                    .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
        } else if (promotion.discountFlat() != null) {
            return promotion.discountFlat().min(totalAmount);
        }
        return BigDecimal.ZERO;
    }
}
