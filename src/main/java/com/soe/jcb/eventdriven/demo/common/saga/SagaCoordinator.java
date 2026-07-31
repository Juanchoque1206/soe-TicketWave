package com.soe.jcb.eventdriven.demo.common.saga;

import com.soe.jcb.eventdriven.demo.common.event.DomainEventBus;
import com.soe.jcb.eventdriven.demo.common.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.payment.event.PaymentRefundedEvent;
import com.soe.jcb.eventdriven.demo.ticket.event.TicketCompensationRequestedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SagaCoordinator {

    private static final Logger log = LoggerFactory.getLogger(SagaCoordinator.class);

    private static final List<SagaStatus> FORWARD_SEQUENCE = List.of(
            SagaStatus.CREATED,
            SagaStatus.ORDER_SUBMITTED,
            SagaStatus.PAYMENT_COMPLETED,
            SagaStatus.ORDER_CONFIRMED,
            SagaStatus.TICKETS_ISSUED,
            SagaStatus.COMPLETED
    );

    private final OrderSagaRepository sagaRepository;
    private final DomainEventBus eventBus;

    public SagaCoordinator(OrderSagaRepository sagaRepository, DomainEventBus eventBus) {
        this.sagaRepository = sagaRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public OrderSaga createSaga(Long orderId, String orderNumber, Long userId) {
        OrderSaga saga = new OrderSaga();
        saga.setOrderId(orderId);
        saga.setOrderNumber(orderNumber);
        saga.setUserId(userId);
        saga.setStatus(SagaStatus.CREATED);
        saga = sagaRepository.save(saga);
        log.info("Saga created for order {} (sagaId={}, status={})", orderNumber, saga.getId(), saga.getStatus());
        return saga;
    }

    @Transactional
    public OrderSaga advanceSaga(Long orderId, SagaStatus targetStatus) {
        OrderSaga saga = sagaRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderSaga", "orderId", orderId));

        int currentIdx = FORWARD_SEQUENCE.indexOf(saga.getStatus());
        int targetIdx = FORWARD_SEQUENCE.indexOf(targetStatus);

        if (targetIdx < 0) {
            log.warn("Target status {} is not in forward sequence; forcing advance", targetStatus);
            saga.setStatus(targetStatus);
        } else if (targetIdx == currentIdx + 1 || targetIdx > currentIdx) {
            saga.setStatus(targetStatus);
        } else {
            log.warn("Saga {}: skipping from {} to {} (expected next: {})",
                    orderId, saga.getStatus(), targetStatus,
                    currentIdx + 1 < FORWARD_SEQUENCE.size() ? FORWARD_SEQUENCE.get(currentIdx + 1) : "COMPLETED");
            saga.setStatus(targetStatus);
        }

        saga = sagaRepository.save(saga);
        log.info("Saga advanced: order {} -> {}", orderNumber(saga), saga.getStatus());
        return saga;
    }

    @Transactional
    public OrderSaga failSaga(Long orderId, String errorMessage) {
        OrderSaga saga = sagaRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderSaga", "orderId", orderId));

        saga.setStatus(SagaStatus.FAILED);
        saga.setLastErrorMessage(errorMessage);
        saga = sagaRepository.save(saga);

        log.warn("Saga FAILED for order {}: {}", orderNumber(saga), errorMessage);
        return saga;
    }

    @Transactional
    public void compensate(Long orderId) {
        OrderSaga saga = sagaRepository.findByOrderId(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("OrderSaga", "orderId", orderId));

        log.warn("Starting compensation for saga of order {}", orderNumber(saga));
        saga.setStatus(SagaStatus.COMPENSATING);
        sagaRepository.save(saga);

        SagaStatus currentStatus = saga.getStatus();

        try {
            if (currentStatus.ordinal() >= SagaStatus.TICKETS_ISSUED.ordinal()
                    || saga.getStatus() == SagaStatus.COMPENSATING) {
                log.info("Compensation step 1: revoking tickets for order {}", orderNumber(saga));
                eventBus.publish(new TicketCompensationRequestedEvent(orderId, orderNumber(saga)));
            }

            if (currentStatus.ordinal() >= SagaStatus.PAYMENT_COMPLETED.ordinal()) {
                log.info("Compensation step 2: refunding payment for order {}", orderNumber(saga));
                saga.getOrderId();
            }

            saga.setStatus(SagaStatus.COMPENSATED);
            sagaRepository.save(saga);
            log.info("Compensation complete for order {}", orderNumber(saga));
        } catch (Exception e) {
            log.error("Compensation failed for order {}: {}", orderNumber(saga), e.getMessage());
            saga.setLastErrorMessage("Compensation error: " + e.getMessage());
            sagaRepository.save(saga);
        }
    }

    @Transactional(readOnly = true)
    public OrderSaga findByOrderId(Long orderId) {
        return sagaRepository.findByOrderId(orderId)
                .orElse(null);
    }

    private String orderNumber(OrderSaga saga) {
        return saga.getOrderNumber();
    }
}
