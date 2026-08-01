package com.ticketwave.application.dto;

import java.io.Serializable;
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
) implements Serializable {}
