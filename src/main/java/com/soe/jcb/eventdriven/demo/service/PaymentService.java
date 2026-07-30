package com.soe.jcb.eventdriven.demo.service;

import com.soe.jcb.eventdriven.demo.dto.PaymentRequest;
import com.soe.jcb.eventdriven.demo.dto.PaymentResponse;
import com.soe.jcb.eventdriven.demo.dto.RefundRequest;
import com.soe.jcb.eventdriven.demo.dto.RefundResponse;

public interface PaymentService {

    PaymentResponse processPayment(String orderNumber, PaymentRequest request);

    RefundResponse processRefund(String orderNumber, RefundRequest request);
}
