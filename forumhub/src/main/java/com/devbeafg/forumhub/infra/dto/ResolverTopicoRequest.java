package com.devbeafg.forumhub.infra.dto;

import jakarta.validation.constraints.NotNull;

public record ResolverTopicoRequest(
        @NotNull(message = "ID da resposta é obrigatório") Long respostaId) {
}
