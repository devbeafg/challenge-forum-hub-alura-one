package com.devbeafg.forumhub.infra.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AtualizacaoTopicoRequest(
        @NotBlank String titulo,
        @NotBlank String mensagem,
        @NotNull Long autorId,
        @NotNull Long cursoId,
        String status) {
}
