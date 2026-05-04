package com.root.calculadoraedenred.dto;

import com.root.calculadoraedenred.model.enums.PaymentType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemCalculoRequest {

    @NotNull(message = "paymentType é obrigatório")
    private PaymentType paymentType;

    @NotNull(message = "quantidade é obrigatória")
    @Min(value = 0, message = "quantidade não pode ser negativa")
    private Long quantidade;
}
