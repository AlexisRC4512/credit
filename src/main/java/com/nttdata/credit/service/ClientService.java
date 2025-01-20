package com.nttdata.credit.service;

import com.nttdata.credit.model.entity.Client;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
@RequiredArgsConstructor
@Service
public class ClientService {
    private final WebClient webClient;

    public Mono<Client> getClientById(String clientId ,String authorizationHeader) {
        return webClient.get()
                .uri("/api/v1/client/{id}", clientId)
                .header("Authorization", authorizationHeader)
                .retrieve()
                .bodyToMono(Client.class);
    }
}
