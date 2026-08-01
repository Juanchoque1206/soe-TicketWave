package com.ticketwave.infrastructure.config;

import com.ticketwave.domain.antifraud.service.FraudDetectionService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AntifraudModuleConfig {

    @Bean
    public FraudDetectionService fraudDetectionService() {
        return new FraudDetectionService();
    }
}
