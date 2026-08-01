package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.PaymentRequest;
import com.ticketwave.application.dto.PaymentResponse;

public interface ProcessPaymentUseCase {

    PaymentResponse process(String orderNumber, PaymentRequest request);
}
