package com.ticketwave.interfaces.rest;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.application.dto.VenueRequest;
import com.ticketwave.application.dto.VenueResponse;
import com.ticketwave.application.usecase.CreateVenueUseCase;
import com.ticketwave.application.usecase.FindVenueUseCase;
import com.ticketwave.application.usecase.SearchVenuesUseCase;
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

    private final CreateVenueUseCase createVenueUseCase;
    private final FindVenueUseCase findVenueUseCase;
    private final SearchVenuesUseCase searchVenuesUseCase;

    public VenueController(CreateVenueUseCase createVenueUseCase,
                          FindVenueUseCase findVenueUseCase,
                          SearchVenuesUseCase searchVenuesUseCase) {
        this.createVenueUseCase = createVenueUseCase;
        this.findVenueUseCase = findVenueUseCase;
        this.searchVenuesUseCase = searchVenuesUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<VenueResponse> create(@Valid @RequestBody VenueRequest request) {
        return ApiResponse.ok("Venue created successfully", createVenueUseCase.create(request));
    }

    @GetMapping("/{id}")
    public ApiResponse<VenueResponse> findById(@PathVariable Long id) {
        return ApiResponse.ok(findVenueUseCase.findById(id));
    }

    @GetMapping
    public ApiResponse<List<VenueResponse>> findVenues(@RequestParam(required = false) String city) {
        if (city != null) {
            return ApiResponse.ok(searchVenuesUseCase.findByCity(city));
        }
        return ApiResponse.ok(searchVenuesUseCase.findAll());
    }
}
