package com.example.EstateHub_Backend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI estateHubOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("EstateHub API")
                        .description("Real Estate Lead Management & Brokerage Platform API")
                        .version("1.0"));
    }
}