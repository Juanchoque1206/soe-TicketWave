package com.ticketwave.application.mapper;

import com.ticketwave.application.dto.SectionResponse;
import com.ticketwave.application.dto.VenueResponse;
import com.ticketwave.domain.venue.model.Section;
import com.ticketwave.domain.venue.model.Venue;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class VenueMapper {

    public VenueResponse toResponse(Venue venue) {
        List<SectionResponse> sections = venue.getSections().stream()
                .map(this::toSectionResponse)
                .toList();
        return new VenueResponse(
                venue.getId(), venue.getName(), venue.getCity(),
                venue.getAddress(), venue.getCapacity(),
                venue.isHasAssignedSeating(), sections);
    }

    public VenueResponse toResponseWithoutSections(Venue venue) {
        return new VenueResponse(
                venue.getId(), venue.getName(), venue.getCity(),
                venue.getAddress(), venue.getCapacity(),
                venue.isHasAssignedSeating(), List.of());
    }

    public SectionResponse toSectionResponse(Section section) {
        return new SectionResponse(
                section.getId(), section.getName(),
                section.getCapacity(), section.isGeneralAdmission(),
                section.getSeats().size());
    }
}
