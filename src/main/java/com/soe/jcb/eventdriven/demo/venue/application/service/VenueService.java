package com.soe.jcb.eventdriven.demo.venue.application.service;

import com.soe.jcb.eventdriven.demo.venue.application.in.VenueUseCase;
import com.soe.jcb.eventdriven.demo.venue.domain.Seat;
import com.soe.jcb.eventdriven.demo.venue.domain.Section;
import com.soe.jcb.eventdriven.demo.venue.domain.Venue;
import com.soe.jcb.eventdriven.demo.venue.domain.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Application service orchestrating venue use-cases. Pure orchestration on
 * top of the domain port; the repository adapter is injected by dependency
 * inversion.
 */
@Service
public class VenueService implements VenueUseCase {

    private final VenueRepository venueRepository;

    public VenueService(VenueRepository venueRepository) {
        this.venueRepository = venueRepository;
    }

    @Override
    @Transactional
    public CreateResult create(CreateVenueCommand command) {
        Venue venue = new Venue(null, command.name(), command.city(), command.address(),
                command.capacity(), command.hasAssignedSeating());

        if (command.sections() != null) {
            for (SectionCommand s : command.sections()) {
                Section section = new Section(null, s.name(), s.capacity(), s.generalAdmission());
                if (s.seats() != null && !s.generalAdmission()) {
                    for (SeatCommand seatCmd : s.seats()) {
                        section.addSeat(new Seat(null, seatCmd.row(), seatCmd.number()));
                    }
                }
                venue.addSection(section);
            }
        }

        venue = venueRepository.save(venue);
        return new CreateResult(venue.getId(), venue.getName(), venue.getCity(),
                venue.getAddress(), venue.getCapacity(), venue.isHasAssignedSeating(),
                venue.getSections());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venue> find(String city) {
        return city == null ? venueRepository.findAll() : venueRepository.findByCity(city);
    }

    @Override
    @Transactional(readOnly = true)
    public Venue findById(Long id) {
        return venueRepository.findById(id).orElse(null);
    }

    @Override
    public List<Venue> findAll() {
        return venueRepository.findAll();
    }
}