package com.soe.jcb.eventdriven.demo.event.interfaces.web;

import com.soe.jcb.eventdriven.demo.common.domain.PageResult;
import com.soe.jcb.eventdriven.demo.common.interfaces.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.common.interfaces.dto.PagedResponse;
import com.soe.jcb.eventdriven.demo.event.application.in.EventUseCase;
import com.soe.jcb.eventdriven.demo.event.domain.Event;
import com.soe.jcb.eventdriven.demo.event.interfaces.dto.EventRequest;
import com.soe.jcb.eventdriven.demo.event.interfaces.dto.EventResponse;
import jakarta.validation.Valid;
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
import java.util.List;

@RestController
@RequestMapping("/api/v1/events")
public class EventController {

    private final EventUseCase eventUseCase;

    public EventController(EventUseCase eventUseCase) {
        this.eventUseCase = eventUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<EventResponse> create(@Valid @RequestBody EventRequest request) {
        EventUseCase.EventResult event = eventUseCase.create(request.toCreateCommand());
        return ApiResponse.ok("Event created successfully", EventResponse.from(event));
    }

    @PutMapping("/{id}")
    public ApiResponse<EventResponse> update(@PathVariable Long id,
                                             @RequestBody EventRequest request) {
        EventUseCase.EventResult event = eventUseCase.update(id,
                request.toUpdateCommand(Event.Status.PUBLISHED));
        return ApiResponse.ok(EventResponse.from(event));
    }

    @GetMapping("/{id}")
    public ApiResponse<EventResponse> findById(@PathVariable Long id) {
        return ApiResponse.ok(EventResponse.from(eventUseCase.findById(id)));
    }

    @GetMapping
    public ApiResponse<PagedResponse<EventResponse>> search(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String artist,
            @RequestParam(required = false) Long venueId,
            @RequestParam(required = false) LocalDateTime from,
            @RequestParam(required = false) LocalDateTime to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {
        EventUseCase.EventQuery query = new EventUseCase.EventQuery(city, artist, venueId, from, to, page, size);
        PageResult<EventUseCase.EventResult> result = eventUseCase.search(query);
        List<EventResponse> events = result.content().stream().map(EventResponse::from).toList();
        return ApiResponse.ok(new PagedResponse<>(events, result.page(), result.size(),
                result.totalElements(), result.totalPages()));
    }

    @PatchMapping("/{id}/cancel")
    public ApiResponse<EventResponse> cancel(@PathVariable Long id) {
        return ApiResponse.ok("Event cancelled", EventResponse.from(eventUseCase.cancel(id)));
    }

    @PatchMapping("/{id}/postpone")
    public ApiResponse<EventResponse> postpone(@PathVariable Long id,
                                               @RequestParam LocalDateTime newDate) {
        return ApiResponse.ok("Event postponed", EventResponse.from(eventUseCase.postpone(id, newDate)));
    }
}