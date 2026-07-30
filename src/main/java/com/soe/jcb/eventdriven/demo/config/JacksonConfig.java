package com.soe.jcb.eventdriven.demo.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    // Jackson auto-configuration is handled by Spring Boot.
    // Date format configured via application.properties:
    //   spring.jackson.serialization.write-dates-as-timestamps=false
}
