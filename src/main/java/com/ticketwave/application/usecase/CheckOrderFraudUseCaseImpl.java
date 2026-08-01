package com.ticketwave.application.usecase;

import com.ticketwave.domain.antifraud.model.FraudCheckResult;
import com.ticketwave.application.usecase.CheckOrderFraudUseCase;
import com.ticketwave.domain.antifraud.repository.OrderActivityPort;
import com.ticketwave.domain.antifraud.service.FraudDetectionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CheckOrderFraudUseCaseImpl implements CheckOrderFraudUseCase {

    private final FraudDetectionService fraudDetectionService;
    private final OrderActivityPort orderActivityPort;

    public CheckOrderFraudUseCaseImpl(FraudDetectionService fraudDetectionService,
                                       OrderActivityPort orderActivityPort) {
        this.fraudDetectionService = fraudDetectionService;
        this.orderActivityPort = orderActivityPort;
    }

    @Override
    @Transactional(readOnly = true)
    public FraudCheckResult checkOrderFraud(Long userId) {
        LocalDateTime since = LocalDateTime.now().minusMinutes(fraudDetectionService.getFraudCheckMinutes());
        long pendingCount = orderActivityPort.countPendingOrdersSince(userId, since);
        return fraudDetectionService.checkOrderFraud(pendingCount);
    }
}
