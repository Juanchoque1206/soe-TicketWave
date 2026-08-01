package com.ticketwave.infrastructure.messaging;

import com.ticketwave.application.usecase.ConfirmOrderUseCase;
import com.ticketwave.application.usecase.IssueTicketsUseCase;
import com.ticketwave.application.usecase.RefundTicketsUseCase;
import com.ticketwave.application.usecase.SendCancellationNoticeUseCase;
import com.ticketwave.application.usecase.SendPurchaseConfirmationUseCase;
import com.ticketwave.domain.common.event.DomainEvent;
import com.ticketwave.domain.payment.event.PaymentCompletedEvent;
import com.ticketwave.domain.payment.event.PaymentRefundedEvent;
import com.ticketwave.domain.ticket.event.OrderCancelledEvent;
import com.ticketwave.domain.ticket.event.OrderConfirmedEvent;
import com.ticketwave.infrastructure.config.RabbitMQConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnProperty(name = "eventbus.type", havingValue = "rabbitmq")
public class RabbitMQEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQEventConsumer.class);

    private final ConfirmOrderUseCase confirmOrderUseCase;
    private final IssueTicketsUseCase issueTicketsUseCase;
    private final RefundTicketsUseCase refundTicketsUseCase;
    private final SendPurchaseConfirmationUseCase sendPurchaseConfirmationUseCase;
    private final SendCancellationNoticeUseCase sendCancellationNoticeUseCase;

    public RabbitMQEventConsumer(ConfirmOrderUseCase confirmOrderUseCase,
                                 IssueTicketsUseCase issueTicketsUseCase,
                                 RefundTicketsUseCase refundTicketsUseCase,
                                 SendPurchaseConfirmationUseCase sendPurchaseConfirmationUseCase,
                                 SendCancellationNoticeUseCase sendCancellationNoticeUseCase) {
        this.confirmOrderUseCase = confirmOrderUseCase;
        this.issueTicketsUseCase = issueTicketsUseCase;
        this.refundTicketsUseCase = refundTicketsUseCase;
        this.sendPurchaseConfirmationUseCase = sendPurchaseConfirmationUseCase;
        this.sendCancellationNoticeUseCase = sendCancellationNoticeUseCase;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAYMENT)
    public void onPayment(DomainEvent event) {
        if (event instanceof PaymentCompletedEvent completed) {
            log.info("Consuming PaymentCompletedEvent for order {}", completed.getOrderNumber());
            confirmOrderUseCase.confirm(completed.getOrderId());
        } else if (event instanceof PaymentRefundedEvent refunded) {
            log.info("Consuming PaymentRefundedEvent for order {}", refunded.getOrderNumber());
            refundTicketsUseCase.refund(refunded.getOrderId());
        } else {
            log.warn("Unhandled event on payment queue: {}", event.getClass().getSimpleName());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_ORDER)
    public void onOrder(DomainEvent event) {
        if (event instanceof OrderConfirmedEvent confirmed) {
            log.info("Consuming OrderConfirmedEvent for order {}", confirmed.getOrderNumber());
            issueTicketsUseCase.issue(confirmed.getOrderId());
        } else {
            log.warn("Unhandled event on order queue: {}", event.getClass().getSimpleName());
        }
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NOTIFICATION)
    public void onNotification(DomainEvent event) {
        if (event instanceof OrderConfirmedEvent confirmed) {
            log.info("Confirming purchase to customer for order {}", confirmed.getOrderNumber());
            sendPurchaseConfirmationUseCase.sendPurchaseConfirmation(
                    confirmed.getUserId(), confirmed.getOrderId());
        } else if (event instanceof OrderCancelledEvent cancelled) {
            log.info("Notifying cancellation to customer for order {}", cancelled.getOrderNumber());
            sendCancellationNoticeUseCase.sendCancellationNotice(
                    cancelled.getUserId(), cancelled.getOrderId());
        } else {
            log.warn("Unhandled event on notification queue: {}", event.getClass().getSimpleName());
        }
    }
}
