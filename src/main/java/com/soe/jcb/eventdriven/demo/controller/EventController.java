package com.soe.jcb.eventdriven.demo.controller;

import com.soe.jcb.eventdriven.demo.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.dto.EventCreateRequest;
import com.soe.jcb.eventdriven.demo.dto.EventResponse;
import com.soe.jcb.eventdriven.demo.dto.EventSummaryResponse;
import com.soe.jcb.eventdriven.demo.dto.EventUpdateRequest;
import com.soe.jcb.eventdriven.demo.dto.PagedResponse;
import com.soe.jcb.eventdriven.demo.service.EventService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EventResponse> create(@Valid @RequestBody EventCreateRequest request) {
        EventResponse event = eventService.create(request);
        return ApiResponse.ok("Event created successfully", event);
    }

    @PutMapping("/{id}")
    public ApiResponse<EventResponse> update(@PathVariable Long id,
                                              @RequestBody EventUpdateRequest request) {
        return ApiResponse.ok(eventService.update(id, request));
    }

    @GetMapping("/{id}")
    public ApiResponse<EventResponse> findById(@PathVariable Long id) {
        return ApiResponse.ok(eventService.findById(id));
    }

    @GetMapping
    public ApiResponse<PagedResponse<EventSummaryResponse>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) Long venueId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @PageableDefault(size = 20) Pageable pageable) {
        return ApiResponse.ok(eventService.search(city, artist, venueId, from, to, pageable));
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<EventResponse> cancel(@PathVariable Long id) {
        return ApiResponse.ok("Event cancelled", eventService.cancel(id));
    }

    @PatchMapping("/{id}/postpone")
    public ApiResponse<EventResponse> postpone(@PathVariable Long id,
                                                @RequestParam LocalDateTime newDate) {
        return ApiResponse.ok("Event postponed", eventService.postpone(id, newDate));
    }
}
