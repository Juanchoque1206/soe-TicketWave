package com.ticketwave.interfaces.rest;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.application.dto.PaymentRequest;
import com.ticketwave.application.dto.PaymentResponse;
import com.ticketwave.application.dto.RefundRequest;
import com.ticketwave.application.dto.RefundResponse;
import com.ticketwave.application.usecase.ProcessPaymentUseCase;
import com.ticketwave.application.usecase.ProcessRefundUseCase;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final ProcessPaymentUseCase processPaymentUseCase;
    private final ProcessRefundUseCase processRefundUseCase;

    public PaymentController(ProcessPaymentUseCase processPaymentUseCase,
                             ProcessRefundUseCase processRefundUseCase) {
        this.processPaymentUseCase = processPaymentUseCase;
        this.processRefundUseCase = processRefundUseCase;
    }

    @PostMapping("/orders/{orderNumber}")
    public ApiResponse<PaymentResponse> processPayment(@PathVariable String orderNumber,
                                                        @Valid @RequestBody PaymentRequest request) {
        return ApiResponse.ok("Payment processed", processPaymentUseCase.process(orderNumber, request));
    }

    @PostMapping("/orders/{orderNumber}/refund")
    public ApiResponse<RefundResponse> processRefund(@PathVariable String orderNumber,
                                                      @RequestBody RefundRequest request) {
        return ApiResponse.ok("Refund processed", processRefundUseCase.refund(orderNumber, request));
    }
}
