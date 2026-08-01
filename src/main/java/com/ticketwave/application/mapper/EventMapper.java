package com.ticketwave.application.mapper;

import com.ticketwave.application.dto.EventResponse;
import com.ticketwave.application.dto.EventSummaryResponse;
import com.ticketwave.application.dto.TicketTypeResponse;
import com.ticketwave.domain.event.model.Event;
import com.ticketwave.domain.event.model.TicketType;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class EventMapper {

    public EventResponse toResponse(Event event) {
        List<TicketTypeResponse> ticketTypes = event.getTicketTypes().stream()
                .map(this::toTicketTypeResponse)
                .toList();
        return new EventResponse(
                event.getId(), event.getName(), event.getDescription(),
                event.getCategory(), event.getStatus(), event.getArtist(),
                event.getEventDate(), event.getSalesStartDate(), event.getSalesEndDate(),
                event.getVenueId(), event.getVenueName(), event.getVenueCity(),
                ticketTypes);
    }

    public EventSummaryResponse toSummaryResponse(Event event) {
        return new EventSummaryResponse(
                event.getId(), event.getName(), event.getCategory(),
                event.getStatus(), event.getArtist(), event.getEventDate(),
                event.getVenueName(), event.getVenueCity());
    }

    public TicketTypeResponse toTicketTypeResponse(TicketType tt) {
        return new TicketTypeResponse(
                tt.getId(), tt.getName(), tt.getPrice(),
                tt.getTotalQuantity(), tt.getSoldQuantity(), tt.getAvailableQuantity(),
                tt.getSectionId(), tt.getSectionName());
    }
}
