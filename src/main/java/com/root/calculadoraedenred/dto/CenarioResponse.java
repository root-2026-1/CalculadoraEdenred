package com.root.calculadoraedenred.dto;

import com.root.calculadoraedenred.model.SavedScenario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CenarioResponse {
    private Long id;
    private String nome;
    private Long empresaId;
    private Double emissoesAtuaisGramas;
    private Double emissoesSimuladasGramas;
    private Double economiaGramas;
    private Double economiaKg;
    private Double percentualReducao;
    private LocalDateTime criadoEm;

    public static CenarioResponse fromEntity(SavedScenario entity) {
        return new CenarioResponse(
                entity.getId(),
                entity.getNome(),
                entity.getEmpresaId(),
                entity.getEmissoesAtuaisGramas(),
                entity.getEmissoesSimuladasGramas(),
                entity.getEconomiaGramas(),
                entity.getEconomiaGramas() / 1000.0,
                entity.getPercentualReducao(),
                entity.getCriadoEm()
        );
    }
}
