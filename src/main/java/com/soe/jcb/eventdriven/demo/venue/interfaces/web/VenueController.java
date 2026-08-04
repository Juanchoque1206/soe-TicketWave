package com.soe.jcb.eventdriven.demo.venue.interfaces.web;

import com.soe.jcb.eventdriven.demo.common.domain.exception.ResourceNotFoundException;
import com.soe.jcb.eventdriven.demo.common.interfaces.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.venue.application.in.VenueUseCase;
import com.soe.jcb.eventdriven.demo.venue.domain.Venue;
import com.soe.jcb.eventdriven.demo.venue.interfaces.dto.VenueRequest;
import com.soe.jcb.eventdriven.demo.venue.interfaces.dto.VenueResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/venues")
public class VenueController {

    private final VenueUseCase venueUseCase;

    public VenueController(VenueUseCase venueUseCase) {
        this.venueUseCase = venueUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VenueResponse> create(@RequestBody VenueRequest request) {
        VenueUseCase.CreateResult venue = venueUseCase.create(request.toCommand());
        return ApiResponse.ok("Venue created successfully", VenueResponse.from(venue));
    }

    @GetMapping("/{id}")
    public ApiResponse<VenueResponse> findById(@PathVariable Long id) {
        Venue venue = venueUseCase.findById(id);
        if (venue == null) {
            throw new ResourceNotFoundException("Venue", "id", id);
        }
        return ApiResponse.ok(VenueResponse.from(venue, true));
    }

    @GetMapping
    public ApiResponse<List<VenueResponse>> findVenues(@RequestParam(required = false) String city) {
        List<VenueResponse> venues = venueUseCase.find(city).stream()
                .map(v -> VenueResponse.from(v, false))
                .toList();
        return ApiResponse.ok(venues);
    }
}