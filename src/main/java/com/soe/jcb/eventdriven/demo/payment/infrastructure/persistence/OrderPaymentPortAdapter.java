package com.soe.jcb.eventdriven.demo.payment.infrastructure.persistence;

import com.soe.jcb.eventdriven.demo.order.application.in.OrderUseCase;
import com.soe.jcb.eventdriven.demo.payment.application.out.OrderPaymentPort;
import org.springframework.stereotype.Component;

/**
 * Adapter bridging the payment context's {@link OrderPaymentPort} to the order
 * context's application use-cases.
 */
@Component
public class OrderPaymentPortAdapter implements OrderPaymentPort {

    private final OrderUseCase orderUseCase;

    public OrderPaymentPortAdapter(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    @Override
    public OrderSnapshot findByOrderNumber(String orderNumber) {
        OrderUseCase.OrderResult order = orderUseCase.findByOrderNumber(orderNumber);
        return new OrderSnapshot(order.id(), order.orderNumber(), order.status(), order.totalAmount());
    }

    @Override
    public boolean confirmOrder(Long orderId) {
        orderUseCase.confirm(orderId);
        return true;
    }
}