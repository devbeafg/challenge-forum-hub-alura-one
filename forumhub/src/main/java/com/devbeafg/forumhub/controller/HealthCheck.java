package com.devbeafg.forumhub.controller;

import java.time.OffsetDateTime;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/healthCheck")
@Tag(name = "Health Check", description = "Endpoint para verificação de saúde da aplicação")
public class HealthCheck {

    @GetMapping
    @Operation(summary = "Verificar status da API", description = "Retorna o status de saúde da aplicação (endpoint público)")
    @ApiResponse(responseCode = "200", description = "API está funcionando")
    public ResponseEntity<Map<String, Object>> valida() {

        return ResponseEntity.ok(Map.of("status", "UP",
                "app", "Forumhub",
                "timestamp", OffsetDateTime.now().toString()));
    }
}
