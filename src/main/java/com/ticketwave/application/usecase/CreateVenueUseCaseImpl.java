package com.ticketwave.application.usecase;

import com.ticketwave.application.dto.VenueRequest;
import com.ticketwave.application.dto.VenueResponse;
import com.ticketwave.application.mapper.VenueMapper;
import com.ticketwave.domain.venue.model.Seat;
import com.ticketwave.domain.venue.model.Section;
import com.ticketwave.domain.venue.model.Venue;
import com.ticketwave.application.usecase.CreateVenueUseCase;
import com.ticketwave.domain.venue.repository.VenueRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateVenueUseCaseImpl implements CreateVenueUseCase {

    private final VenueRepository venueRepository;
    private final VenueMapper venueMapper;

    public CreateVenueUseCaseImpl(VenueRepository venueRepository, VenueMapper venueMapper) {
        this.venueRepository = venueRepository;
        this.venueMapper = venueMapper;
    }

    @Override
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

                if (sectionReq.seats() != null && !sectionReq.generalAdmission()) {
                    for (VenueRequest.SeatRequest seatReq : sectionReq.seats()) {
                        Seat seat = new Seat();
                        seat.setRow(seatReq.row());
                        seat.setNumber(seatReq.number());
                        section.getSeats().add(seat);
                    }
                }

                venue.getSections().add(section);
            }
        }

        venue = venueRepository.save(venue);
        return venueMapper.toResponse(venue);
    }
}
