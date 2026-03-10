package com.devbeafg.forumhub.infra.dto;

import com.devbeafg.forumhub.domain.model.Perfil;

public record PerfilDTO(Long id, String nome) {

    public static PerfilDTO fromEntity(Perfil perfil) {
        return new PerfilDTO(perfil.getId(), perfil.getNome());
    }
}
