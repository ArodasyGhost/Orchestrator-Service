package br.ucsal.orchestrator_service.client;

import br.ucsal.orchestrator_service.dto.BibliografiaDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "bibliography-service")
public interface BibliographyClient {

    // Supondo que o Bibliography-Service tenha um endpoint que aceita uma lista
    @PostMapping("/bibliografias/batch")
    List<Long> createBibliografias(@RequestBody List<BibliografiaDTO> dtos);
}