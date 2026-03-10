package com.devbeafg.forumhub.infra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CadastroRespostaRequest(
        @NotBlank String mensagem,
        @NotNull Long topicoId,
        @NotNull Long autorId) {
}
