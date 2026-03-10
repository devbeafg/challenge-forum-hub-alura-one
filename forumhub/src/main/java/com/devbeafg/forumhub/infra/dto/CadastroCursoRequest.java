package com.devbeafg.forumhub.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastroCursoRequest(
        @NotBlank(message = "Nome é obrigatório.") String nome,
        @NotBlank(message = "Categoria é obrigatória.") String categoria) {
}
