package com.soe.jcb.eventdriven.demo.promotion.controller;

import com.soe.jcb.eventdriven.demo.common.dto.ApiResponse;
import com.soe.jcb.eventdriven.demo.promotion.dto.PromotionRequest;
import com.soe.jcb.eventdriven.demo.promotion.dto.PromotionResponse;
import com.soe.jcb.eventdriven.demo.promotion.service.PromotionService;
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

    private final PromotionService promotionService;

    public PromotionController(PromotionService promotionService) {
        this.promotionService = promotionService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<PromotionResponse> create(@Valid @RequestBody PromotionRequest request) {
        return ApiResponse.ok("Promotion created", promotionService.create(request));
    }

    @GetMapping(params = "code")
    public ApiResponse<PromotionResponse> validate(@RequestParam String code,
                                                    @RequestParam(required = false) Long venueId) {
        return ApiResponse.ok(promotionService.validate(code, venueId));
    }

    @GetMapping
    public ApiResponse<List<PromotionResponse>> findActive() {
        return ApiResponse.ok(promotionService.findActive());
    }
}
