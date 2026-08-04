package com.soe.jcb.eventdriven.demo.saga.saga_orchestrator;

import com.soe.jcb.eventdriven.demo.ticket.application.in.TicketUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Listener reacting to order domain events (published on the broker) and
 * driving downstream saga steps, e.g. issuing tickets once an order is
 * confirmed. Each handler is idempotent and backed by {@link SagaStateManager}.
 */
@Component
public class SagaOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(SagaOrchestrator.class);

    private final TicketUseCase ticketUseCase;
    private final SagaStateManager sagaStateManager;

    public SagaOrchestrator(TicketUseCase ticketUseCase, SagaStateManager sagaStateManager) {
        this.ticketUseCase = ticketUseCase;
        this.sagaStateManager = sagaStateManager;
    }

    public void handleOrderConfirmed(Long orderId) {
        if (!sagaStateManager.markStepCompleted(orderId, "ticket_issuance")) {
            log.info("Order {} ticket_issuance already completed, skipping", orderId);
            return;
        }
        ticketUseCase.issueTickets(orderId);
        log.info("Saga: issued tickets for order {}", orderId);
    }

    public void handleOrderCancelled(Long orderId) {
        sagaStateManager.removeSagaState(orderId);
        log.info("Saga: order {} cancelled, saga state cleared", orderId);
    }
}