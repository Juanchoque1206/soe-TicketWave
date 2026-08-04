package com.soe.jcb.eventdriven.demo.promotion.interfaces.web;

import com.soe.jcb.eventdriven.demo.common.interfaces.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.promotion.application.in.PromotionUseCase;
import com.soe.jcb.eventdriven.demo.promotion.interfaces.dto.PromotionRequest;
import com.soe.jcb.eventdriven.demo.promotion.interfaces.dto.PromotionResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/promotions")
public class PromotionController {

    private final PromotionUseCase promotionUseCase;

    public PromotionController(PromotionUseCase promotionUseCase) {
        this.promotionUseCase = promotionUseCase;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody PromotionRequest request) {
        PromotionUseCase.PromotionResult result = promotionUseCase.create(request.toCommand());
        return ApiResponse.ok("Promotion created", PromotionResponse.from(result));
    }

    @GetMapping(params = "code")
    public ApiResponse<PromotionResponse> validate(@RequestParam String code,
                                                   @RequestParam(required = false) Long venueId) {
        return ApiResponse.ok(PromotionResponse.from(promotionUseCase.validate(code, venueId)));
    }

    @GetMapping
    public ApiResponse<List<PromotionResponse>> findActive() {
        return ApiResponse.ok(promotionUseCase.findActive().stream().map(PromotionResponse::from).toList());
    }
}