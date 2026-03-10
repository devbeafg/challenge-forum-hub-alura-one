package com.devbeafg.forumhub.infra.dto;

import java.util.List;
import java.util.stream.Collectors;

import com.devbeafg.forumhub.domain.model.Usuario;

public record UsuarioRespostaDTO(Long id, String nome, String email, List<PerfilDTO> perfis) {
    public static UsuarioRespostaDTO fromEntity(Usuario usuario) {
        List<PerfilDTO> perfisDTO = usuario.getPerfis() != null
                ? usuario.getPerfis().stream()
                        .map(PerfilDTO::fromEntity)
                        .collect(Collectors.toList())
                : List.of();

        return new UsuarioRespostaDTO(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                perfisDTO);
    }
}
