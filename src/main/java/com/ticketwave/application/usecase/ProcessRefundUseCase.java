package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.RefundRequest;
import com.ticketwave.application.dto.RefundResponse;

public interface ProcessRefundUseCase {

    RefundResponse refund(String orderNumber, RefundRequest request);
}
