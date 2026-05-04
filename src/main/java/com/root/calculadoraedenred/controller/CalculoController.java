package com.root.calculadoraedenred.controller;

import com.root.calculadoraedenred.dto.CalculoRequest;
import com.root.calculadoraedenred.dto.CalculoResponse;
import com.root.calculadoraedenred.service.CalculoEmissoesService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/calculos")
public class CalculoController {

    private final CalculoEmissoesService service;

    public CalculoController(CalculoEmissoesService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<CalculoResponse> calcular(@Valid @RequestBody CalculoRequest request) {
        return ResponseEntity.ok(service.calcular(request));
    }
}
