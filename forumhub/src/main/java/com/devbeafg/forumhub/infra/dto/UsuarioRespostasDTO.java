package com.devbeafg.forumhub.infra.dto;

import java.time.LocalDateTime;

import com.devbeafg.forumhub.domain.model.Resposta;

public record UsuarioRespostasDTO(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        Boolean solucao,
        TopicoResumoDTO topico) {
    public static UsuarioRespostasDTO fromEntity(Resposta resposta) {
        return new UsuarioRespostasDTO(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getSolucao(),
                new TopicoResumoDTO(resposta.getTopico().getId(), resposta.getTopico().getTitulo()));
    }

    public record TopicoResumoDTO(Long id, String titulo) {
    }
}
