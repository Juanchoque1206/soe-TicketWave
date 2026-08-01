package com.ticketwave.interfaces.rest;

import com.ticketwave.application.dto.ApiResponse;
import com.ticketwave.application.dto.PromotionRequest;
import com.ticketwave.application.dto.PromotionResponse;
import com.ticketwave.application.usecase.CreatePromotionUseCase;
import com.ticketwave.application.usecase.FindActivePromotionsUseCase;
import com.ticketwave.application.usecase.ValidatePromotionUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final CreatePromotionUseCase createPromotionUseCase;
    private final ValidatePromotionUseCase validatePromotionUseCase;
    private final FindActivePromotionsUseCase findActivePromotionsUseCase;

    public PromotionController(CreatePromotionUseCase createPromotionUseCase,
                              ValidatePromotionUseCase validatePromotionUseCase,
                              FindActivePromotionsUseCase findActivePromotionsUseCase) {
        this.createPromotionUseCase = createPromotionUseCase;
        this.validatePromotionUseCase = validatePromotionUseCase;
        this.findActivePromotionsUseCase = findActivePromotionsUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody PromotionRequest request) {
        return ApiResponse.ok("Promotion created", createPromotionUseCase.create(request));
    }

    @GetMapping(params = "code")
    public ApiResponse<PromotionResponse> validate(@RequestParam String code,
                                                    @RequestParam(required = false) Long venueId) {
        return ApiResponse.ok(validatePromotionUseCase.validate(code, venueId));
    }

    @GetMapping
    public ApiResponse<List<PromotionResponse>> findActive() {
        return ApiResponse.ok(findActivePromotionsUseCase.findActive());
    }
}
