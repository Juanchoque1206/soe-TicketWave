package com.soe.jcb.eventdriven.demo.payment.service;

import com.soe.jcb.eventdriven.demo.payment.dto.PaymentRequest;
import com.soe.jcb.eventdriven.demo.payment.dto.PaymentResponse;
import com.soe.jcb.eventdriven.demo.payment.dto.RefundRequest;
import com.soe.jcb.eventdriven.demo.payment.dto.RefundResponse;

public interface PaymentService {

    PaymentResponse processPayment(String orderNumber, PaymentRequest request);

    RefundResponse processRefund(String orderNumber, RefundRequest request);
}
