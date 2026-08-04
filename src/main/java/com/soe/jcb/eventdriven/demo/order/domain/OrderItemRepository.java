package com.soe.jcb.eventdriven.demo.order.domain;

/**
 * Domain port for querying active seat reservations in the order context.
 */
public interface OrderItemRepository {

    /**
     * @return true when a PENDING/CONFIRMED order line already reserves the
     *         given seat for the requested event.
     */
    boolean isSeatReserved(Long eventId, Long seatId);
}