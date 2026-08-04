package com.soe.jcb.eventdriven.demo.venue.application.in;

import com.soe.jcb.eventdriven.demo.venue.domain.Section;
import com.soe.jcb.eventdriven.demo.venue.domain.Venue;
import java.util.List;

/**
 * Use-case boundary for creating and querying venues (pure, no Spring).
 */
public interface VenueUseCase {

    CreateResult create(CreateVenueCommand command);

    List<Venue> find(String city);

    Venue findById(Long id);

    List<Venue> findAll();

    record CreateVenueCommand(String name, String city, String address, int capacity,
                              boolean hasAssignedSeating, List<SectionCommand> sections) {
    }

    record SectionCommand(String name, int capacity, boolean generalAdmission, List<SeatCommand> seats) {
    }

    record SeatCommand(String row, int number) {
    }

    record CreateResult(Long id, String name, String city, String address,
                        int capacity, boolean hasAssignedSeating, List<Section> sections) {
    }
}