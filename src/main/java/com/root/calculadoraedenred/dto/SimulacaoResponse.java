package com.root.calculadoraedenred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SimulacaoResponse {
    private Long empresaId;
    private CalculoResponse cenarioAtual;
    private CalculoResponse cenarioSimulado;
    private Double economiaGramas;
    private Double economiaKg;
    private Double percentualReducao;
}
