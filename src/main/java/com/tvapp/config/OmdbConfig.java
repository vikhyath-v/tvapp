package com.tvapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.context.annotation.Bean;

@Configuration
public class OmdbConfig {
    
    @Value("${omdb.api.key}")
    private String apiKey;
    
    @Value("${omdb.api.url}")
    private String apiUrl;
    
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(apiUrl)
                .build();
    }
} 