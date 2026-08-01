package com.ticketwave.infrastructure.config;

import com.ticketwave.domain.promotion.service.DiscountCalculator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PromotionModuleConfig {

    @Bean
    public DiscountCalculator discountCalculator() {
        return new DiscountCalculator();
    }
}
