package br.ucsal.orchestrator_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "auth-service") // Nome do serviço no Eureka
public interface AuthClient {

    // Supondo que o Auth-Service tenha um endpoint /validate
    @GetMapping("/auth/validate")
    void validateToken(@RequestHeader("Authorization") String token);
}