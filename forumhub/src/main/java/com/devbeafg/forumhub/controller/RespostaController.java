package com.devbeafg.forumhub.controller;

import java.net.URI;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devbeafg.forumhub.domain.model.Resposta;
import com.devbeafg.forumhub.infra.dto.AtualizacaoRespostaRequest;
import com.devbeafg.forumhub.infra.dto.CadastroRespostaRequest;
import com.devbeafg.forumhub.infra.dto.RespostaDTO;
import com.devbeafg.forumhub.service.resposta.CadastroRespostaService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/respostas")
@Tag(name = "Respostas", description = "Endpoints para gerenciamento de respostas aos tópicos")
@SecurityRequirement(name = "bearerAuth")
public class RespostaController {

    private final CadastroRespostaService service;

    public RespostaController(CadastroRespostaService service) {
        this.service = service;
    }

    @PostMapping
    @Operation(summary = "Criar nova resposta", description = "Cria uma nova resposta para um tópico")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resposta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "401", description = "Não autenticado")
    })
    public ResponseEntity<?> cadastrar(@RequestBody @Valid CadastroRespostaRequest request) throws Exception {
        Resposta criada = service.cadastrarResposta(request);
        RespostaDTO response = RespostaDTO.fromEntity(criada);

        URI location = URI.create("/respostas/" + criada.getId());
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping
    @Operation(summary = "Listar respostas", description = "Lista todas as respostas com paginação, opcionalmente filtradas por usuário")
    @ApiResponse(responseCode = "200", description = "Lista de respostas retornada com sucesso")
    public ResponseEntity<?> listar(
            @Parameter(description = "ID do usuário para filtrar respostas") @RequestParam(required = false) Long usuarioId,
            @PageableDefault(size = 10, sort = "dataCriacao", direction = Sort.Direction.DESC) Pageable paginacao) {
        if (usuarioId != null) {
            return ResponseEntity.ok(service.listarRespostasDoUsuario(usuarioId, paginacao));
        }
        return ResponseEntity.ok(service.listarRespostas(paginacao));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Detalhar resposta", description = "Retorna os detalhes de uma resposta específica")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta encontrada"),
            @ApiResponse(responseCode = "404", description = "Resposta não encontrada")
    })
    public ResponseEntity<?> detalhar(@PathVariable Long id) {
        RespostaDTO resposta = service.detalharResposta(id);
        return ResponseEntity.ok(resposta);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar resposta", description = "Atualiza os dados de uma resposta existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resposta atualizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Resposta não encontrada")
    })
    public ResponseEntity<?> atualizar(@PathVariable Long id, @RequestBody @Valid AtualizacaoRespostaRequest request)
            throws Exception {
        RespostaDTO resposta = service.atualizarResposta(id, request);
        return ResponseEntity.ok(resposta);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar resposta", description = "Remove uma resposta do sistema")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resposta deletada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Resposta não encontrada")
    })
    public ResponseEntity<?> deletar(@PathVariable Long id) throws Exception {
        service.deletarResposta(id);
        return ResponseEntity.noContent().build();
    }
}
