package br.ucsal.orchestrator_service;

import br.ucsal.orchestrator_service.client.*;
import br.ucsal.orchestrator_service.dto.BibliografiaDTO;
import br.ucsal.orchestrator_service.dto.ProgramaCompletoRequestDTO;
import feign.FeignException;
import feign.Request;
import feign.Request.HttpMethod;
import feign.RequestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.nio.charset.Charset;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

// 1. Carrega o contexto Spring para testar o OrchestrationService
@SpringBootTest
class OrchestrationServiceTest {

    @Autowired
    private OrchestrationService service;

    // 2. Substitui os Feign Clients reais por Mocks. O Spring não os iniciará.
    @MockBean
    private AuthClient authClient;
    @MockBean
    private ProfessorClient professorClient;
    @MockBean
    private DisciplineClient disciplineClient;
    @MockBean
    private BibliographyClient bibliographyClient;
    @MockBean
    private ProgramClient programClient;

    // --- Dados de Simulação ---
    private final String VALID_TOKEN = "Bearer validToken";
    private final Long PROFESSOR_ID = 101L;
    private final Long DISCIPLINA_ID = 202L;

    private ProgramaCompletoRequestDTO createValidRequest() {
        // Cria um DTO de requisição completo com dados de Bibliografia
        ProgramaCompletoRequestDTO request = new ProgramaCompletoRequestDTO();
        request.setProfessorId(PROFESSOR_ID);
        request.setDisciplinaId(DISCIPLINA_ID);
        request.setEmenta("Ementa de Teste");
        request.setAtivo(true);

        BibliografiaDTO bib = new BibliografiaDTO();
        bib.setTitulo("Clean Code");
        bib.setAutor("Robert Martin");

        request.setBibliografiasBasicas(List.of(bib));
        request.setBibliografiasComplementares(Collections.emptyList());
        return request;
    }

    @Test
    void shouldExecuteFullOrchestrationSuccessfully() {
        // ARRANGE (Configurar o ambiente simulado)
        ProgramaCompletoRequestDTO request = createValidRequest();

        // 3. Define as regras de comportamento para os Mocks:
        // Auth-Service: Simula sucesso (nada acontece/retorna)
        doNothing().when(authClient).validateToken(VALID_TOKEN);

        // Professor-Service e Discipline-Service: Simula sucesso (nada acontece/retorna)
        doNothing().when(professorClient).getProfessorById(PROFESSOR_ID);
        doNothing().when(disciplineClient).getDisciplinaById(DISCIPLINA_ID);

        // Bibliography-Service: Simula criação e retorna IDs (1 e 2)
        when(bibliographyClient.createBibliografias(anyList()))
                .thenReturn(List.of(1L, 2L));

        // Program-Service: Simula sucesso (nada acontece/retorna)
        doNothing().when(programClient).createPrograma(any());


        // ACT (Executar o serviço a ser testado)
        assertDoesNotThrow(() -> service.createCompleteProgram(request, VALID_TOKEN));


        // ASSERT (Verificar se a ordem de chamada ocorreu como esperado)
        // 4. Verifica se todos os passos foram chamados:
        verify(authClient, times(1)).validateToken(VALID_TOKEN);
        verify(professorClient, times(1)).getProfessorById(PROFESSOR_ID);
        verify(disciplineClient, times(1)).getDisciplinaById(DISCIPLINA_ID);
        verify(bibliographyClient, times(1)).createBibliografias(anyList());

        // Verifica se o Program-Service foi chamado com o payload final (o resultado da orquestração)
        verify(programClient, times(1)).createPrograma(argThat(dto -> {
            // Verifica se o DTO final contém os IDs da bibliografia gerados
            return dto.getBibliografiaIds().contains(1L) && dto.getBibliografiaIds().contains(2L);
        }));
    }

    @Test
    void shouldThrowExceptionWhenProfessorIdNotFound() {
        // ARRANGE (Configurar o cenário de falha)
        ProgramaCompletoRequestDTO request = createValidRequest();

        // Simula que o Professor-Service retorna 404 (FeignException)
        doThrow(createFeignException(404))
                .when(professorClient).getProfessorById(PROFESSOR_ID);

        // Auth e Discipline ainda simulam sucesso
        doNothing().when(authClient).validateToken(VALID_TOKEN);
        doNothing().when(disciplineClient).getDisciplinaById(DISCIPLINA_ID);


        // ACT & ASSERT (Verificar se o erro é propagado)
        assertThrows(FeignException.class,
                () -> service.createCompleteProgram(request, VALID_TOKEN));

        // Verifica se o Program-Service NUNCA foi chamado (transação abortada)
        verify(programClient, never()).createPrograma(any());

        // Verifica se o Bibliography-Service NUNCA foi chamado (abortado no passo 2)
        verify(bibliographyClient, never()).createBibliografias(anyList());
    }

    // Método auxiliar para criar uma FeignException simulada
    private FeignException createFeignException(int status) {
        return FeignException.errorStatus("mockClient", Response.builder()
                .status(status)
                .request(Request.create(HttpMethod.GET, "/url", Collections.emptyMap(), null, Charset.defaultCharset(), new RequestTemplate()))
                .build());
    }
}