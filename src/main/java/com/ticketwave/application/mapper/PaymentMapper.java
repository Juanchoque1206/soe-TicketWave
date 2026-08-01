package com.ticketwave.application.mapper;

import com.ticketwave.application.dto.PaymentResponse;
import com.ticketwave.domain.payment.model.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrderId(),
                payment.getOrderNumber(),
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus(),
                payment.getExternalTransactionId(),
                payment.getPaidAt());
    }
}
