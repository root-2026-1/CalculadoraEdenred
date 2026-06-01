package com.root.calculadoraedenred.controller;

import com.root.calculadoraedenred.dto.SimulacaoRequest;
import com.root.calculadoraedenred.dto.SimulacaoResponse;
import com.root.calculadoraedenred.service.SimulacaoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulacoes")
public class SimulacaoController {

    private final SimulacaoService simulacaoService;

    public SimulacaoController(SimulacaoService simulacaoService) {
        this.simulacaoService = simulacaoService;
    }

    @PostMapping
    public ResponseEntity<SimulacaoResponse> simular(@Valid @RequestBody SimulacaoRequest request) {
        return ResponseEntity.ok(simulacaoService.simular(request));
    }
}
