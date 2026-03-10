package com.devbeafg.forumhub.infra.dto;

import jakarta.validation.constraints.NotBlank;

public record AtualizacaoRespostaRequest(
        @NotBlank String mensagem,
        Boolean solucao) {
}
