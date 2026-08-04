package com.soe.jcb.eventdriven.demo.venue.interfaces.dto;

import com.soe.jcb.eventdriven.demo.venue.application.in.VenueUseCase;

import java.util.List;

public record VenueRequest(
        String name,
        String city,
        String address,
        int capacity,
        boolean hasAssignedSeating,
        List<SectionRequest> sections
) {
    public VenueUseCase.CreateVenueCommand toCommand() {
        List<VenueUseCase.SectionCommand> sectionCommands = sections == null ? List.of()
                : sections.stream().map(SectionRequest::toCommand).toList();
        return new VenueUseCase.CreateVenueCommand(name, city, address, capacity,
                hasAssignedSeating, sectionCommands);
    }

    public record SectionRequest(
            String name,
            int capacity,
            boolean generalAdmission,
            List<SeatRequest> seats
    ) {
        VenueUseCase.SectionCommand toCommand() {
            List<VenueUseCase.SeatCommand> seatCommands = seats == null ? List.of()
                    : seats.stream().map(SeatRequest::toCommand).toList();
            return new VenueUseCase.SectionCommand(name, capacity, generalAdmission, seatCommands);
        }
    }

    public record SeatRequest(
            String row,
            int number
    ) {
        VenueUseCase.SeatCommand toCommand() {
            return new VenueUseCase.SeatCommand(row, number);
        }
    }
}