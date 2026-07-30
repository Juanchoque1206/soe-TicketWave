package com.soe.jcb.eventdriven.demo.payment.controller;

import com.soe.jcb.eventdriven.demo.common.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.payment.dto.PaymentRequest;
import com.soe.jcb.eventdriven.demo.payment.dto.PaymentResponse;
import com.soe.jcb.eventdriven.demo.payment.dto.RefundRequest;
import com.soe.jcb.eventdriven.demo.payment.dto.RefundResponse;
import com.soe.jcb.eventdriven.demo.payment.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/orders/{orderNumber}")
    public ApiResponse<PaymentResponse> processPayment(@PathVariable String orderNumber,
                                                        @Valid @RequestBody PaymentRequest request) {
        return ApiResponse.ok("Payment processed", paymentService.processPayment(orderNumber, request));
    }

    @PostMapping("/orders/{orderNumber}/refund")
    public ApiResponse<RefundResponse> processRefund(@PathVariable String orderNumber,
                                                      @RequestBody RefundRequest request) {
        return ApiResponse.ok("Refund processed", paymentService.processRefund(orderNumber, request));
    }
}
