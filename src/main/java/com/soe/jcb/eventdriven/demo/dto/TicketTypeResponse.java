package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.TicketType;

import java.math.BigDecimal;

public record TicketTypeResponse(
        Long id,
        String name,
        BigDecimal price,
        int totalQuantity,
        int soldQuantity,
        int availableQuantity,
        Long sectionId,
        String sectionName
) {
    public static TicketTypeResponse from(TicketType ticketType) {
        return new TicketTypeResponse(
                ticketType.getId(),
                ticketType.getName(),
                ticketType.getPrice(),
                ticketType.getTotalQuantity(),
                ticketType.getSoldQuantity(),
                ticketType.getAvailableQuantity(),
                ticketType.getSection() != null ? ticketType.getSection().getId() : null,
                ticketType.getSection() != null ? ticketType.getSection().getName() : null
        );
    }
}
