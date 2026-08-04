package com.soe.jcb.eventdriven.demo.venue.application.service;

import com.soe.jcb.eventdriven.demo.venue.application.in.VenueUseCase;
import com.soe.jcb.eventdriven.demo.venue.domain.Venue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@Transactional
class VenueServiceIntegrationTest {

    @Autowired
    private VenueUseCase venueUseCase;

    @Test
    void createAndFindVenueWithSectionsAndSeats() {
        VenueUseCase.CreateVenueCommand command = new VenueUseCase.CreateVenueCommand(
                "Madison Square Garden", "New York", "4 Penn Plaza", 20000, true,
                List.of(new VenueUseCase.SectionCommand("Floor", 100, false,
                        List.of(new VenueUseCase.SeatCommand("A", 1), new VenueUseCase.SeatCommand("A", 2))),
                        new VenueUseCase.SectionCommand("Balcony", 50, true, List.of())));

        VenueUseCase.CreateResult created = venueUseCase.create(command);

        assertNotNull(created.id());
        assertEquals("Madison Square Garden", created.name());

        VenueUseCase.CreateVenueCommand singleSection = new VenueUseCase.CreateVenueCommand(
                "Arena", "Boston", "1 Causeway St", 5000, true,
                List.of(new VenueUseCase.SectionCommand("GA", 200, true, List.of())));
        venueUseCase.create(singleSection);

        List<Venue> byCity = venueUseCase.find("New York");
        assertEquals(1, byCity.size());
        assertEquals("Madison Square Garden", byCity.get(0).getName());
        assertEquals(2, byCity.get(0).getSections().size());
    }
}