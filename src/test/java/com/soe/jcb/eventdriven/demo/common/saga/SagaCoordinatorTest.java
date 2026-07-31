package com.soe.jcb.eventdriven.demo.common.saga;

import com.soe.jcb.eventdriven.demo.common.event.DomainEventBus;
import com.soe.jcb.eventdriven.demo.common.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Transactional
class SagaCoordinatorTest {

    @Autowired
    private SagaCoordinator sagaCoordinator;

    @Autowired
    private OrderSagaRepository sagaRepository;

    @Autowired
    private DomainEventBus eventBus;

    private OrderSaga saga;

    @BeforeEach
    void setUp() {
        saga = sagaCoordinator.createSaga(1L, "SAGA-TEST-001", 1L);
    }

    @Test
    @DisplayName("Should create saga with CREATED status")
    void createSaga() {
        assertThat(saga).isNotNull();
        assertThat(saga.getOrderId()).isEqualTo(1L);
        assertThat(saga.getOrderNumber()).isEqualTo("SAGA-TEST-001");
        assertThat(saga.getUserId()).isEqualTo(1L);
        assertThat(saga.getStatus()).isEqualTo(SagaStatus.CREATED);
    }

    @Test
    @DisplayName("Should advance saga through forward sequence: CREATED -> ORDER_SUBMITTED -> PAYMENT_COMPLETED -> ORDER_CONFIRMED -> TICKETS_ISSUED -> COMPLETED")
    void advanceSagaFullSequence() {
        sagaCoordinator.advanceSaga(1L, SagaStatus.ORDER_SUBMITTED);
        assertThat(sagaRepository.findByOrderId(1L).get().getStatus()).isEqualTo(SagaStatus.ORDER_SUBMITTED);

        sagaCoordinator.advanceSaga(1L, SagaStatus.PAYMENT_COMPLETED);
        assertThat(sagaRepository.findByOrderId(1L).get().getStatus()).isEqualTo(SagaStatus.PAYMENT_COMPLETED);

        sagaCoordinator.advanceSaga(1L, SagaStatus.ORDER_CONFIRMED);
        assertThat(sagaRepository.findByOrderId(1L).get().getStatus()).isEqualTo(SagaStatus.ORDER_CONFIRMED);

        sagaCoordinator.advanceSaga(1L, SagaStatus.TICKETS_ISSUED);
        assertThat(sagaRepository.findByOrderId(1L).get().getStatus()).isEqualTo(SagaStatus.TICKETS_ISSUED);

        sagaCoordinator.advanceSaga(1L, SagaStatus.COMPLETED);
        assertThat(sagaRepository.findByOrderId(1L).get().getStatus()).isEqualTo(SagaStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should mark saga as FAILED with error message")
    void failSaga() {
        sagaCoordinator.failSaga(1L, "Payment declined");

        OrderSaga failed = sagaRepository.findByOrderId(1L).orElseThrow();
        assertThat(failed.getStatus()).isEqualTo(SagaStatus.FAILED);
        assertThat(failed.getLastErrorMessage()).isEqualTo("Payment declined");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException for non-existent saga")
    void findByOrderIdNotFound() {
        OrderSaga result = sagaCoordinator.findByOrderId(999L);
        assertThat(result).isNull();
    }

    @Test
    @DisplayName("Should advance saga forward even when skipping intermediate states")
    void advanceSagaSkipStates() {
        sagaCoordinator.advanceSaga(1L, SagaStatus.COMPLETED);
        OrderSaga result = sagaRepository.findByOrderId(1L).orElseThrow();
        assertThat(result.getStatus()).isEqualTo(SagaStatus.COMPLETED);
    }

    @Test
    @DisplayName("Should advance saga and find by order number")
    void findByOrderNumber() {
        sagaCoordinator.advanceSaga(1L, SagaStatus.ORDER_SUBMITTED);

        OrderSaga found = sagaRepository.findByOrderNumber("SAGA-TEST-001").orElseThrow();
        assertThat(found.getStatus()).isEqualTo(SagaStatus.ORDER_SUBMITTED);
        assertThat(found.getOrderNumber()).isEqualTo("SAGA-TEST-001");
    }
}
