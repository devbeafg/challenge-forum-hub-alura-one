package com.devbeafg.forumhub.infra.dto;

import java.time.LocalDateTime;

import com.devbeafg.forumhub.domain.model.Resposta;

public record RespostaDTO(
        Long id,
        String mensagem,
        LocalDateTime dataCriacao,
        Boolean solucao,
        UsuarioDTO autor) {
    public static RespostaDTO fromEntity(Resposta resposta) {
        return new RespostaDTO(
                resposta.getId(),
                resposta.getMensagem(),
                resposta.getDataCriacao(),
                resposta.getSolucao(),
                UsuarioDTO.fromEntity(resposta.getAutor()));
    }
}
