package com.devbeafg.forumhub.infra.dto;

import com.devbeafg.forumhub.domain.model.Curso;

public record CursoDTO(Long id, String nome, String categoria) {
    public static CursoDTO fromEntity(Curso curso) {
        return new CursoDTO(curso.getId(), curso.getNome(), curso.getCategoria());
    }
}
