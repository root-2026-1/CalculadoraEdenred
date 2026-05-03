package com.root.calculadoraedenred.controller;

import com.root.calculadoraedenred.dto.SustainabilityScoreResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/companies")
public class SustainabilityController {

    @GetMapping("/{companyId}/sustainability-score")
    public ResponseEntity<SustainabilityScoreResponse> getScore(@PathVariable Long companyId) {
        // TODO: chamar o service de cálculo de score
        return ResponseEntity.ok().build();
    }
}
