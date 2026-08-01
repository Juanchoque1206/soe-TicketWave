package com.ticketwave.application.usecase;

import com.ticketwave.domain.antifraud.model.FraudCheckResult;

public interface CheckOrderFraudUseCase {
    FraudCheckResult checkOrderFraud(Long userId);
}
