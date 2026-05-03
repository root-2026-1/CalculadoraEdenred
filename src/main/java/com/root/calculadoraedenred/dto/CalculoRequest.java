package com.root.calculadoraedenred.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CalculoRequest {

    @NotNull(message = "empresaId é obrigatório")
    private Long empresaId;

    @NotEmpty(message = "itens não pode ser vazio")
    @Valid
    private List<ItemCalculoRequest> itens;
}
