package br.ucsal.orchestrator_service.service;

import br.ucsal.orchestrator_service.client.*;
import br.ucsal.orchestrator_service.dto.ProgramaCompletoRequestDTO;
import br.ucsal.orchestrator_service.dto.ProgramaRequestDTO; // NOVO DTO DE PAYLOAD
import feign.FeignException;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrchestrationService {

    private final AuthClient authClient;
    private final ProfessorClient professorClient;
    private final DisciplineClient disciplineClient;
    private final BibliographyClient bibliographyClient;
    private final ProgramClient programClient;

    public void createCompleteProgram(ProgramaCompletoRequestDTO request, String token) throws FeignException {

        // 1. Validação do Token e Entidades (Fluxo de Validação)
        authClient.validateToken(token);
        professorClient.getProfessorById(request.getProfessorId());
        disciplineClient.getDisciplinaById(request.getDisciplinaId());

        // 2. Criação da Bibliografia (Retorna IDs)
        List<Long> bibliografiaIds = bibliographyClient.createBibliografias(
                request.getBibliografiasBasicas()
        );

        // 3. Montagem e Criação do Programa (USANDO ProgramaRequestDTO)

        // O Program-Service usa ProgramRequestDTO para receber dados
        ProgramaRequestDTO programaPayload = ProgramaRequestDTO.builder()
                // Campos específicos do Program-Service (snake_case)
                .disciplina_id(request.getDisciplinaId().intValue()) // Assumindo que DisciplinaId é Long, mas o DTO espera Integer
                .matriz_id(request.getMatrizId())
                .semestre(request.getSemestre())

                // Campos de conteúdo
                .ementa(request.getEmenta())
                .objetivos(request.getObjetivos())
                .conteudoProgramatico(request.getConteudoProgramatico())
                .metodologia(request.getMetodologia())
                .avaliacao(request.getAvaliacao())
                .ativo(request.getAtivo())

                // Dados extras usados para auditoria/fluxo no Orchestrator (se for usado no ProgramRequestDTO)
                .professorId(request.getProfessorId())
                .bibliografiaIds(bibliografiaIds) // IDs de bibliografia retornados
                .build();

        programClient.createPrograma(programaPayload);
    }
}