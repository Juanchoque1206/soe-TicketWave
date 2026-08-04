package com.soe.jcb.eventdriven.demo.payment.interfaces.web;

import com.soe.jcb.eventdriven.demo.common.interfaces.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.payment.application.in.PaymentUseCase;
import com.soe.jcb.eventdriven.demo.payment.interfaces.dto.PaymentRequest;
import com.soe.jcb.eventdriven.demo.payment.interfaces.dto.PaymentResponse;
import com.soe.jcb.eventdriven.demo.payment.interfaces.dto.RefundRequest;
import com.soe.jcb.eventdriven.demo.payment.interfaces.dto.RefundResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentUseCase paymentUseCase;

    public PaymentController(PaymentUseCase paymentUseCase) {
        this.paymentUseCase = paymentUseCase;
    }

    @PostMapping("/orders/{orderNumber}")
    public ApiResponse<PaymentResponse> processPayment(@PathVariable String orderNumber,
                                                       @Valid @RequestBody PaymentRequest request) {
        PaymentUseCase.PaymentResult result = paymentUseCase.processPayment(orderNumber, request.toCommand());
        return ApiResponse.ok("Payment processed", PaymentResponse.from(result));
    }

    @PostMapping("/orders/{orderNumber}/refund")
    public ApiResponse<RefundResponse> processRefund(@PathVariable String orderNumber,
                                                     @RequestBody RefundRequest request) {
        return ApiResponse.ok("Refund processed",
                RefundResponse.from(paymentUseCase.processRefund(orderNumber,
                        new PaymentUseCase.RefundCommand(request.reason()))));
    }
}