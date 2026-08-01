package com.ticketwave.infrastructure.messaging;

import com.ticketwave.domain.payment.event.PaymentCompletedEvent;
import com.ticketwave.domain.payment.event.PaymentRefundedEvent;
import com.ticketwave.domain.ticket.event.OrderConfirmedEvent;
import com.ticketwave.application.usecase.ConfirmOrderUseCase;
import com.ticketwave.application.usecase.IssueTicketsUseCase;
import com.ticketwave.application.usecase.RefundTicketsUseCase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class OrderEventProcessor {

    private static final Logger log = LoggerFactory.getLogger(OrderEventProcessor.class);

    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final IssueTicketsUseCase issueTicketsUseCase;
    private final RefundTicketsUseCase refundTicketsUseCase;

    public OrderEventProcessor(ConfirmOrderUseCase confirmOrderUseCase,
                               IssueTicketsUseCase issueTicketsUseCase,
                               RefundTicketsUseCase refundTicketsUseCase) {
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.issueTicketsUseCase = issueTicketsUseCase;
        this.refundTicketsUseCase = refundTicketsUseCase;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPaymentCompleted(PaymentCompletedEvent event) {
        log.info("Handling PaymentCompletedEvent for order {}", event.getOrderNumber());
        confirmOrderUseCase.confirm(event.getOrderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onPaymentRefunded(PaymentRefundedEvent event) {
        log.info("Handling PaymentRefundedEvent for order {}", event.getOrderNumber());
        refundTicketsUseCase.refund(event.getOrderId());
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void onOrderConfirmed(OrderConfirmedEvent event) {
        log.info("Handling OrderConfirmedEvent for order {}", event.getOrderNumber());
        issueTicketsUseCase.issue(event.getOrderId());
    }
}
