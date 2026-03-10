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
import com.devbeafg.forumhub.domain.model.Curso;
import com.devbeafg.forumhub.infra.dto.CadastroCursoRequest;
import com.devbeafg.forumhub.infra.dto.CursoDTO;
import com.devbeafg.forumhub.service.curso.CadastroCursoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/cursos")
@Tag(name = "Cursos", description = "Endpoints para gerenciamento de cursos")
@SecurityRequirement(name = "bearerAuth")
public class CursoController {

    private final CadastroCursoService service;

    public CursoController(CadastroCursoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo curso", description = "Cria um novo curso no sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Curso criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida ou curso duplicado")
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroCursoRequest request) {
        try {
            Curso criado = service.cadastrarCurso(request);
            URI location = URI.create("/cursos/" + criado.getId());

            return ResponseEntity.created(location).body(CursoDTO.fromEntity(criado));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    @Operation(summary = "Listar cursos", description = "Lista todos os cursos com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de cursos retornada com sucesso")
    public ResponseEntity<?> listar(
            @PageableDefault(size = 10, sort = "nome", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(service.listarCursos(paginacao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar curso", description = "Retorna os detalhes de um curso específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        try {
            Curso curso = service.detalharCurso(id);
            return ResponseEntity.ok(CursoDTO.fromEntity(curso));
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar curso", description = "Atualiza os dados de um curso existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado"),
            @ApiResponse(responseCode = "400", description = "Curso duplicado")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid CadastroCursoRequest request) {
        try {
            Curso atualizado = service.atualizarCurso(id, request);
            return ResponseEntity.ok(CursoDTO.fromEntity(atualizado));
        } catch (RegraNegocioException e) {
            HttpStatus status = "Curso não encontrado.".equals(e.getMessage())
                    ? HttpStatus.NOT_FOUND
                    : HttpStatus.BAD_REQUEST;
            return ResponseEntity.status(status).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar curso", description = "Remove um curso do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Curso deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Curso não encontrado")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletarCurso(id);
            return ResponseEntity.noContent().build();
        } catch (RegraNegocioException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
