package br.ucsal.orchestrator_service.controller; // <- ATUALIZADO

import br.ucsal.orchestrator_service.dto.ProgramaCompletoRequestDTO; // <- ATUALIZADO
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orchestrator") // Rota interna do orquestrador
@RequiredArgsConstructor
public class OrchestrationController {

    private final OrchestrationService orchestrationService;

    // O Gateway irá rotear de /api/gateway/programas-completos para este endpoint
    @PostMapping("/programas-completos")
    public ResponseEntity<Void> createCompleteProgram(
            @RequestBody ProgramaCompletoRequestDTO request,
            @RequestHeader("Authorization") String token) {

        orchestrationService.createCompleteProgram(request, token);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}