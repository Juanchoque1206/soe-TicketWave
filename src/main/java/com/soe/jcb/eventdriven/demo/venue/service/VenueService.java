package com.soe.jcb.eventdriven.demo.venue.service;

import com.soe.jcb.eventdriven.demo.venue.dto.VenueRequest;
import com.soe.jcb.eventdriven.demo.venue.dto.VenueResponse;
import com.soe.jcb.eventdriven.demo.venue.entity.Seat;
import com.soe.jcb.eventdriven.demo.venue.entity.Section;
import com.soe.jcb.eventdriven.demo.venue.entity.Venue;
import com.soe.jcb.eventdriven.demo.common.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.venue.repository.SectionRepository;
import com.soe.jcb.eventdriven.demo.venue.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class VenueService {

    private final VenueRepository venueRepository;
    private final SectionRepository sectionRepository;

    public VenueService(VenueRepository venueRepository, SectionRepository sectionRepository) {
        this.venueRepository = venueRepository;
        this.sectionRepository = sectionRepository;
    }

    @Transactional
    public VenueResponse create(VenueRequest request) {
        Venue venue = new Venue();
        venue.setName(request.name());
        venue.setCity(request.city());
        venue.setAddress(request.address());
        venue.setCapacity(request.capacity());
        venue.setHasAssignedSeating(request.hasAssignedSeating());

        if (request.sections() != null) {
            for (VenueRequest.SectionRequest sectionReq : request.sections()) {
                Section section = new Section();
                section.setName(sectionReq.name());
                section.setCapacity(sectionReq.capacity());
                section.setGeneralAdmission(sectionReq.generalAdmission());
                section.setVenue(venue);

                if (sectionReq.seats() != null && !sectionReq.generalAdmission()) {
                    for (VenueRequest.SeatRequest seatReq : sectionReq.seats()) {
                        Seat seat = new Seat();
                        seat.setRow(seatReq.row());
                        seat.setNumber(seatReq.number());
                        seat.setSection(section);
                        section.getSeats().add(seat);
                    }
                }

                venue.getSections().add(section);
            }
        }

        venue = venueRepository.save(venue);
        return VenueResponse.from(venue);
    }

    @Transactional(readOnly = true)
    public VenueResponse findById(Long id) {
        Venue venue = venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue", "id", id));
        return VenueResponse.from(venue);
    }

    @Transactional(readOnly = true)
    public List<VenueResponse> findByCity(String city) {
        return venueRepository.findByCity(city).stream()
                .map(VenueResponse::fromWithoutSections)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<VenueResponse> findAll() {
        return venueRepository.findAll().stream()
                .map(VenueResponse::fromWithoutSections)
                .toList();
    }
}
