package com.devbeafg.forumhub.infra.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import com.devbeafg.forumhub.domain.model.Topico;

public record TopicoRespostaDTO(
        Long id,
        String titulo,
        String mensagem,
        LocalDateTime dataCriacao,
        String status,
        UsuarioDTO autor,
        CursoDTO curso,
        List<RespostaDTO> respostas) {
    public static TopicoRespostaDTO fromEntity(Topico topico) {
        List<RespostaDTO> respostasDTO = topico.getRespostas() != null
                ? topico.getRespostas().stream()
                        .map(RespostaDTO::fromEntity)
                        .collect(Collectors.toList())
                : List.of();

        return new TopicoRespostaDTO(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensagem(),
                topico.getDataCriacao(),
                topico.getStatus(),
                UsuarioDTO.fromEntity(topico.getAutor()),
                CursoDTO.fromEntity(topico.getCurso()),
                respostasDTO);
    }
}