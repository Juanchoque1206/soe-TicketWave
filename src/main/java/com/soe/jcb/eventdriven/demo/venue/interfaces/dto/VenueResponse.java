package com.soe.jcb.eventdriven.demo.venue.interfaces.dto;

import com.soe.jcb.eventdriven.demo.venue.application.in.VenueUseCase;
import com.soe.jcb.eventdriven.demo.venue.domain.Section;
import com.soe.jcb.eventdriven.demo.venue.domain.Venue;

import java.util.List;

public record VenueResponse(
        Long id,
        String name,
        String city,
        String address,
        int capacity,
        boolean hasAssignedSeating,
        List<SectionResponse> sections
) {
    public static VenueResponse from(VenueUseCase.CreateResult result) {
        List<SectionResponse> sections = result.sections() == null ? List.of()
                : result.sections().stream().map(SectionResponse::from).toList();
        return new VenueResponse(result.id(), result.name(), result.city(), result.address(),
                result.capacity(), result.hasAssignedSeating(), sections);
    }

    public static VenueResponse from(Venue venue, boolean withSections) {
        List<SectionResponse> sections = withSections
                ? venue.getSections().stream().map(SectionResponse::from).toList()
                : List.<SectionResponse>of();
        return new VenueResponse(venue.getId(), venue.getName(), venue.getCity(), venue.getAddress(),
                venue.getCapacity(), venue.isHasAssignedSeating(), sections);
    }

    public record SectionResponse(
            Long id,
            String name,
            int capacity,
            boolean generalAdmission,
            int seatCount
    ) {
        public static SectionResponse from(Section section) {
            return new SectionResponse(section.getId(), section.getName(), section.getCapacity(),
                    section.isGeneralAdmission(), section.getSeats().size());
        }
    }
}