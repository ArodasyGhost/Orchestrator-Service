package br.ucsal.orchestrator_service.client;

import br.ucsal.orchestrator_service.dto.ProgramaRequestDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "program-service")
public interface ProgramClient {

    @PostMapping("/programas")
    void createPrograma(@RequestBody ProgramaRequestDTO dto);
}