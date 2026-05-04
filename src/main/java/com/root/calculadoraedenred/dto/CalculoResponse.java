package com.root.calculadoraedenred.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoResponse {
    private Long empresaId;
    private Double totalEmissoesGramas;
    private Double totalEmissoesKg;
    private List<ItemDetalhamentoResponse> detalhamentoPorTipo;
}
