package com.devbeafg.forumhub.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record CadastroPerfilRequest(
        @NotBlank(message = "Nome é obrigatório.") String nome) {
}
