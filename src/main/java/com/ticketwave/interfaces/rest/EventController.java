package com.ticketwave.interfaces.rest;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.application.dto.PagedResponse;
import com.ticketwave.application.dto.*;
import com.ticketwave.application.usecase.*;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final CreateEventUseCase createEventUseCase;
    private final UpdateEventUseCase updateEventUseCase;
    private final FindEventUseCase findEventUseCase;
    private final SearchEventsUseCase searchEventsUseCase;
    private final CancelEventUseCase cancelEventUseCase;
    private final PostponeEventUseCase postponeEventUseCase;

    public EventController(CreateEventUseCase createEventUseCase,
                          UpdateEventUseCase updateEventUseCase,
                          FindEventUseCase findEventUseCase,
                          SearchEventsUseCase searchEventsUseCase,
                          CancelEventUseCase cancelEventUseCase,
                          PostponeEventUseCase postponeEventUseCase) {
        this.createEventUseCase = createEventUseCase;
        this.updateEventUseCase = updateEventUseCase;
        this.findEventUseCase = findEventUseCase;
        this.searchEventsUseCase = searchEventsUseCase;
        this.cancelEventUseCase = cancelEventUseCase;
        this.postponeEventUseCase = postponeEventUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EventResponse> create(@Valid @RequestBody EventCreateRequest request) {
        return ApiResponse.ok("Event created successfully", createEventUseCase.create(request));
    }

    @PutMapping("/{id}")
    public ApiResponse<EventResponse> update(@PathVariable Long id, @RequestBody EventUpdateRequest request) {
        return ApiResponse.ok(updateEventUseCase.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<EventResponse> findById(@PathVariable Long id) {
        return ApiResponse.ok(findEventUseCase.findById(id));
    }

    @GetMapping
    public ApiResponse<PagedResponse<EventSummaryResponse>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) Long venueId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(searchEventsUseCase.search(city, artist, venueId, from, to, pageable));
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<EventResponse> cancel(@PathVariable Long id) {
        return ApiResponse.ok("Event cancelled", cancelEventUseCase.cancel(id));
    }

    @PatchMapping("/{id}/postpone")
    public ApiResponse<EventResponse> postpone(@PathVariable Long id, @RequestParam LocalDateTime newDate) {
        return ApiResponse.ok("Event postponed", postponeEventUseCase.postpone(id, newDate));
    }
}
