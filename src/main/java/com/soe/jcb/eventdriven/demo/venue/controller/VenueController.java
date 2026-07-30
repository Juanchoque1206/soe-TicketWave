package com.soe.jcb.eventdriven.demo.venue.controller;

import com.soe.jcb.eventdriven.demo.common.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.venue.dto.VenueRequest;
import com.soe.jcb.eventdriven.demo.venue.dto.VenueResponse;
import com.soe.jcb.eventdriven.demo.venue.service.VenueService;
import jakarta.validation.Valid;
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

    private final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        VenueResponse venue = venueService.create(request);
        return ApiResponse.ok("Venue created successfully", venue);
    }

    @GetMapping("/{id}")
    public ApiResponse<VenueResponse> findById(@PathVariable Long id) {
        return ApiResponse.ok(venueService.findById(id));
    }

    @GetMapping
    public ApiResponse<List<VenueResponse>> findVenues(
            @RequestParam(required = false) String city) {
        if (city != null) {
            return ApiResponse.ok(venueService.findByCity(city));
        }
        return ApiResponse.ok(venueService.findAll());
    }
}
