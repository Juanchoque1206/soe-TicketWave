package com.ticketwave.infrastructure.config;

import com.ticketwave.domain.event.service.EventStatusMachine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EventModuleConfig {

    @Bean
    public EventStatusMachine eventStatusMachine() {
        return new EventStatusMachine();
    }
}
