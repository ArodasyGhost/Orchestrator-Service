package br.ucsal.orchestrator_service.dto;

import lombok.Data;
import java.util.List;

@Data
public class ProgramaCompletoRequestDTO {
    // IDs de validação
    private Long professorId;
    private Long disciplinaId;

    // CAMPOS ADICIONAIS DO PROGRAM-SERVICE (BASEADO EM Program-Service-main/src/main/java/br/ucsal/Program_service/dto/ProgramaDTO.java)
    private Integer matrizId; // Mapeia para matriz_id no Program-Service
    private Integer semestre;
    private String conteudoProgramatico;
    private String avaliacao;
    private Boolean ativo; // Usado para o campo 'ativo'

    // Dados para novas entidades (Bibliography-Service)
    private List<BibliografiaDTO> bibliografiasBasicas;
    private List<BibliografiaDTO> bibliografiasComplementares;

    // Dados do próprio programa (já existentes)
    private String ementa;
    private String objetivos;
    private String metodologia;
}