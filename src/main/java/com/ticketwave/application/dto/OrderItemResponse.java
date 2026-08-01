package com.ticketwave.application.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        Long ticketTypeId,
        String ticketTypeName,
        BigDecimal unitPrice,
        Long seatId,
        String seatRow,
        Integer seatNumber
) implements Serializable {
}
