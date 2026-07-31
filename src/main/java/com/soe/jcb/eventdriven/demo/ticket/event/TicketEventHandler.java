package com.soe.jcb.eventdriven.demo.ticket.event;

import com.soe.jcb.eventdriven.demo.common.event.DomainEventBus;
import com.soe.jcb.eventdriven.demo.common.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.common.saga.SagaCoordinator;
import com.soe.jcb.eventdriven.demo.common.saga.SagaFailedEvent;
import com.soe.jcb.eventdriven.demo.common.saga.SagaStatus;
import com.soe.jcb.eventdriven.demo.order.entity.Order;
import com.soe.jcb.eventdriven.demo.order.event.OrderConfirmedEvent;
import com.soe.jcb.eventdriven.demo.order.repository.OrderRepository;
import com.soe.jcb.eventdriven.demo.ticket.service.TicketService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class TicketEventHandler {

    private static final Logger log = LoggerFactory.getLogger(TicketEventHandler.class);

    private final TicketService ticketService;
    private final OrderRepository orderRepository;
    private final DomainEventBus eventBus;
    private final SagaCoordinator sagaCoordinator;

    public TicketEventHandler(TicketService ticketService,
                              OrderRepository orderRepository,
                              DomainEventBus eventBus,
                              SagaCoordinator sagaCoordinator) {
        this.ticketService = ticketService;
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
        this.sagaCoordinator = sagaCoordinator;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Handling OrderConfirmedEvent for order {}", event.getOrderNumber());
        try {
            Order order = orderRepository.findById(event.getOrderId())
                    .orElseThrow(() -> new ResourceNotFoundException("Order", "id", event.getOrderId()));
            var tickets = ticketService.issueTickets(order);
            sagaCoordinator.advanceSaga(event.getOrderId(), SagaStatus.TICKETS_ISSUED);
            var ticketIds = tickets.stream().map(t -> t.id()).toList();
            eventBus.publish(new TicketsIssuedEvent(
                    event.getOrderId(), event.getOrderNumber(), event.getUserId(), ticketIds));
            sagaCoordinator.advanceSaga(event.getOrderId(), SagaStatus.COMPLETED);
        } catch (Exception e) {
            log.error("Failed to issue tickets for order {}: {}", event.getOrderNumber(), e.getMessage());
            sagaCoordinator.failSaga(event.getOrderId(), "Ticket issuance failed: " + e.getMessage());
            eventBus.publish(new SagaFailedEvent(
                    event.getOrderId(), event.getOrderNumber(), event.getUserId(),
                    "Ticket issuance failed: " + e.getMessage(), "TICKET_ISSUANCE"));
        }
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onTicketCompensationRequested(TicketCompensationRequestedEvent event) {
        log.warn("Compensating tickets for order {}", event.getOrderNumber());
        try {
            ticketService.revokeTickets(event.getOrderId());
            log.info("Tickets revoked for order {}", event.getOrderNumber());
        } catch (Exception e) {
            log.error("Failed to revoke tickets for order {}: {}", event.getOrderNumber(), e.getMessage());
        }
    }
}
