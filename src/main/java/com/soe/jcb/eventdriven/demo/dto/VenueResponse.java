package com.soe.jcb.eventdriven.demo.dto;

import com.soe.jcb.eventdriven.demo.entity.Venue;

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
    public static VenueResponse from(Venue venue) {
        List<SectionResponse> sections = venue.getSections().stream()
                .map(SectionResponse::from)
                .toList();
        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getAddress(),
                venue.getCapacity(),
                venue.isHasAssignedSeating(),
                sections
        );
    }

    public static VenueResponse fromWithoutSections(Venue venue) {
        return new VenueResponse(
                venue.getId(),
                venue.getName(),
                venue.getCity(),
                venue.getAddress(),
                venue.getCapacity(),
                venue.isHasAssignedSeating(),
                List.of()
        );
    }
}
