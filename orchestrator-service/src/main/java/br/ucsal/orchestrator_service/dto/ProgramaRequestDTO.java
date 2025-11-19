package br.ucsal.orchestrator_service.dto;

import lombok.Builder;
import lombok.Data;
import java.util.List;

@Data
@Builder
public class ProgramaRequestDTO {
    // DADOS EXATOS DO PROGRAMA DTO (Program-Service)
    // Usando snake_case para os IDs internos do Program-Service para maior compatibilidade.
    private Integer disciplina_id;
    private Integer matriz_id;
    private Integer semestre;
    private String ementa;
    private String objetivos;
    private String conteudoProgramatico;
    private String metodologia;
    private String avaliacao;
    private Boolean ativo;

    // DADOS EXTRAS USADOS PELO ORQUESTRADOR PARA AUDITORIA/FLUXO
    private Long professorId; // Usado para autenticação/logs (não no ProgramaDTO)
    private List<Long> bibliografiaIds; // Usado para associações futuras (não no ProgramaDTO)
}