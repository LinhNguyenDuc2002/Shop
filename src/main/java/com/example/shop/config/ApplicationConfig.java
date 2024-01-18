package com.example.shop.config;

import lombok.Getter;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@Getter
public class ApplicationConfig {
    @Value("${tempUser.lifespan:12}")
    private int tempUserLifespanInHour;

    /**
     * Encode password
     * @return BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    /**
     * Swagger config
     * @return
     */
    @Bean
    public GroupedOpenApi controllerApi() {
        return GroupedOpenApi.builder()
                .group("Api")
                .packagesToScan("com.example.shop.controller") // Specify the package to scan
                .build();
    }
}
