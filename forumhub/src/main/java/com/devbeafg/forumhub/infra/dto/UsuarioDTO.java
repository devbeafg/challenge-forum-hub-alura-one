package com.devbeafg.forumhub.infra.dto;

import com.devbeafg.forumhub.domain.model.Usuario;

public record UsuarioDTO(Long id, String nome) {
    public static UsuarioDTO fromEntity(Usuario usuario) {
        return new UsuarioDTO(usuario.getId(), usuario.getNome());
    }
}
