package com.devbeafg.forumhub.infra.dto;

import java.util.List;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record CadastroUsuarioRequest(
                @NotBlank(message = "Nome é obrigatório.") String nome,

                @NotBlank(message = "Email é obrigatório.") @Email(message = "Email inválido.") String email,

                @NotBlank(message = "Senha é obrigatória.") @Size(max = 16, message = "Senha deve ter no máximo 16 caracteres.") String senha,

                @NotEmpty(message = "É necessário informar pelo menos um perfil.") List<Long> perfilIds) {
}
