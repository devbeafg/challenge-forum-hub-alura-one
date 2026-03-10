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

import com.devbeafg.forumhub.domain.model.Topico;
import com.devbeafg.forumhub.infra.dto.AtualizacaoTopicoRequest;
import com.devbeafg.forumhub.infra.dto.CadastroTopicoRequest;
import com.devbeafg.forumhub.infra.dto.ResolverTopicoRequest;
import com.devbeafg.forumhub.infra.dto.TopicoRespostaDTO;
import com.devbeafg.forumhub.service.topico.CadastroTopicoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/topicos")
@Tag(name = "Tópicos", description = "Endpoints para gerenciamento de tópicos do fórum")
@SecurityRequirement(name = "bearerAuth")
public class TopicoController {

    private final CadastroTopicoService service;

    private TopicoController(CadastroTopicoService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar novo tópico", description = "Cria um novo tópico no fórum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Tópico criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroTopicoRequest request) throws Exception {
        Topico criado = service.cadastrarTopico(request);
        TopicoRespostaDTO response = TopicoRespostaDTO.fromEntity(criado);

        URI location = URI.create("/topicos/" + criado.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar tópicos", description = "Lista todos os tópicos com paginação")
    @ApiResponse(responseCode = "200", description = "Lista de tópicos retornada com sucesso")
    public ResponseEntity<?> listar(
            @PageableDefault(size = 10, sort = "titulo", direction = Sort.Direction.ASC) Pageable paginacao) {
        return ResponseEntity.ok(service.listarTopicos(paginacao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar tópico", description = "Retorna os detalhes de um tópico específico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tópico encontrado"),
            @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
    })
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        try {
            Topico topico = service.detalharTopico(id);
            return ResponseEntity.ok(TopicoRespostaDTO.fromEntity(topico));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Tópico não encontrado.");
        }

    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar tópico", description = "Atualiza os dados de um tópico existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tópico atualizado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoTopicoRequest request) {
        try {

            Topico atualizado = service.atualizarTopico(id, request);

            return ResponseEntity.ok(TopicoRespostaDTO.fromEntity(atualizado));
        } catch (Exception e) {
            if ("Tópico não encontrado.".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("" + e.getMessage());
        }
    }

    @PutMapping("/{topicoId}/resolver")
    @Operation(summary = "Resolver tópico", description = "Marca um tópico como resolvido e a resposta como solução")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Tópico resolvido com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Tópico ou resposta não encontrado"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<?> resolver(@PathVariable Long topicoId,
            @RequestBody @Valid ResolverTopicoRequest request) {
        try {
            Topico topico = service.resolverTopico(topicoId, request.respostaId());
            return ResponseEntity.ok(TopicoRespostaDTO.fromEntity(topico));
        } catch (Exception e) {
            if ("Tópico não encontrado.".equals(e.getMessage()) ||
                    "Resposta não encontrada.".equals(e.getMessage()) ||
                    "Resposta não pertence a este tópico.".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao resolver o tópico: " + e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar tópico", description = "Remove um tópico do fórum")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Tópico deletado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Tópico não encontrado")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) {
        try {
            service.deletarTopico(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            if ("Tópico não encontrado.".equals(e.getMessage())) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao deletar o tópico: " + e.getMessage());
        }
    }
}
