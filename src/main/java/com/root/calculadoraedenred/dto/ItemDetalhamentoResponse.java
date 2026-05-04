package com.root.calculadoraedenred.dto;

import com.root.calculadoraedenred.model.enums.PaymentType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemDetalhamentoResponse {
    private PaymentType paymentType;
    private Long quantidade;
    private Double co2GramasPorTransacao;
    private Double emissoesGramas;
}
