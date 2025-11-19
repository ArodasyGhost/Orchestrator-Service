package br.ucsal.orchestrator_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "discipline-service")
public interface DisciplineClient {

    @GetMapping("/disciplinas/{id}")
    void getDisciplinaById(@PathVariable Long id); // Só precisamos que dê 200 OK
}