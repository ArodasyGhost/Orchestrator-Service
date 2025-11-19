package br.ucsal.orchestrator_service.dto;

import lombok.Data;

@Data
public class BibliografiaDTO {
    private String titulo;
    private String autor;
    private String ano;

    // CAMPOS DO BIBLIOGRAPHY-SERVICE:
    private String editora;
    private String edicao;
    private String link;
}