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
import com.devbeafg.forumhub.domain.model.Perfil;
import com.devbeafg.forumhub.infra.dto.CadastroPerfilRequest;
import com.devbeafg.forumhub.infra.dto.PerfilDTO;
import com.devbeafg.forumhub.service.perfil.CadastroPerfilService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/perfis")
@Tag(name = "Perfis", description = "Endpoints para gerenciamento de perfis de usuários")
@SecurityRequirement(name = "bearerAuth")
public class PerfilController {

    private final CadastroPerfilService service;

    public PerfilController(CadastroPerfilService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo perfil", description = "Cria um novo perfil de usuário no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Perfil criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou perfil duplicado")
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroPerfilRequest request) {
        try {
            Perfil criado = service.cadastrarPerfil(request);
            URI location = URI.create("/perfis/" + criado.getId());

            return ResponseEntity.created(location).body(PerfilDTO.fromEntity(criado));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar perfis", description = "Lista todos os perfis com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de perfis retornada com sucesso")
    public ResponseEntity<?> listar(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(service.listarPerfis(paginacao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar perfil", description = "Retorna os detalhes de um perfil específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil encontrado"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        try {
            Perfil perfil = service.detalharPerfil(id);
            return ResponseEntity.ok(PerfilDTO.fromEntity(perfil));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar perfil", description = "Atualiza os dados de um perfil existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Perfil atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado"),
            @ApiResponse(responseCode = "400", description = "Perfil duplicado")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid CadastroPerfilRequest request) {
        try {
            Perfil atualizado = service.atualizarPerfil(id, request);
            return ResponseEntity.ok(PerfilDTO.fromEntity(atualizado));
        } catch (RegraNegocioException e) {
            HttpStatus status = "Perfil não encontrado.".equals(e.getMessage())
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar perfil", description = "Remove um perfil do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Perfil deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Perfil não encontrado")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletarPerfil(id);
            return ResponseEntity.noContent().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
