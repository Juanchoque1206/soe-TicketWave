package com.soe.jcb.eventdriven.demo.ticket.event;

import com.soe.jcb.eventdriven.demo.common.event.DomainEventBus;
import com.soe.jcb.eventdriven.demo.common.exception.ResourceNotFoundException;
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

    public TicketEventHandler(TicketService ticketService,
                              OrderRepository orderRepository,
                              DomainEventBus eventBus) {
        this.ticketService = ticketService;
        this.orderRepository = orderRepository;
        this.eventBus = eventBus;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Handling OrderConfirmedEvent for order {}", event.getOrderNumber());
        Order order = orderRepository.findById(event.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order", "id", event.getOrderId()));
        var tickets = ticketService.issueTickets(order);
        var ticketIds = tickets.stream().map(t -> t.id()).toList();
        eventBus.publish(new TicketsIssuedEvent(
                event.getOrderId(), event.getOrderNumber(), event.getUserId(), ticketIds));
    }
}
