package com.nttdata.credit.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${server.url.client}")
    private String clientUrl;
    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(clientUrl)
                .defaultHeader("Content-Type", "application/json")
                .build();
    }
}
