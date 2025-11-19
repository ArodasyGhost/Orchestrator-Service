package br.ucsal.orchestrator_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "professor-service")
public interface ProfessorClient {

    // Supondo que o Professor-Service tenha um GET /professores/{id}
    @GetMapping("/professores/{id}")
    void getProfessorById(@PathVariable Long id); // Só precisamos que dê 200 OK
}