package com.devbeafg.forumhub.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devbeafg.forumhub.domain.Exceptions.RegraNegocioException;
import com.devbeafg.forumhub.domain.model.Usuario;
import com.devbeafg.forumhub.infra.dto.CadastroUsuarioRequest;
import com.devbeafg.forumhub.infra.dto.UsuarioRespostaDTO;
import com.devbeafg.forumhub.service.usuario.CadastroUsuarioService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuarios")
@Tag(name = "Usuários", description = "Endpoints para gerenciamento de usuários")
public class UsuarioController {

    private final CadastroUsuarioService service;

    public UsuarioController(CadastroUsuarioService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Registrar novo usuário", description = "Cria um novo usuário no sistema (endpoint público)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou email já cadastrado")
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroUsuarioRequest request) {
        try {
            Usuario criado = service.cadastrarUsuario(request);
            URI location = URI.create("/usuarios/" + criado.getId());

            return ResponseEntity.created(location).body(UsuarioRespostaDTO.fromEntity(criado));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar usuários", description = "Lista todos os usuários com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> listar(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(service.listarUsuarios(paginacao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar usuário", description = "Retorna os detalhes de um usuário específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        try {
            Usuario usuario = service.detalharUsuario(id);
            return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(usuario));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar usuário", description = "Atualiza os dados de um usuário existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado"),
            @ApiResponse(responseCode = "400", description = "Email já cadastrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid CadastroUsuarioRequest request) {
        try {
            Usuario atualizado = service.atualizarUsuario(id, request);
            return ResponseEntity.ok(UsuarioRespostaDTO.fromEntity(atualizado));
        } catch (RegraNegocioException e) {
            HttpStatus status = "Usuário não encontrado.".equals(e.getMessage())
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar usuário", description = "Remove um usuário do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletarUsuario(id);
            return ResponseEntity.noContent().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
