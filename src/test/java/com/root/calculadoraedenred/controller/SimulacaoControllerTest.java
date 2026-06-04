package com.root.calculadoraedenred.controller;

import com.root.calculadoraedenred.dto.CalculoResponse;
import com.root.calculadoraedenred.dto.ItemDetalhamentoResponse;
import com.root.calculadoraedenred.dto.SimulacaoResponse;
import com.root.calculadoraedenred.model.enums.PaymentType;
import com.root.calculadoraedenred.service.SimulacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SimulacaoController.class)
class SimulacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SimulacaoService simulacaoService;

    private static final String REQUEST_JSON = """
            {
              "empresaId": 42,
              "distribuicaoAtual": [
                { "paymentType": "PHYSICAL", "quantidade": 1000 }
              ],
              "distribuicaoSimulada": [
                { "paymentType": "PIX", "quantidade": 1000 }
              ]
            }
            """;

    @Test
    void retornaSimulacaoComEconomia() throws Exception {
        CalculoResponse atual = new CalculoResponse(42L, 980.0, 0.98, List.of(
                new ItemDetalhamentoResponse(PaymentType.PHYSICAL, 1000L, 0.98, 980.0)), null);
        CalculoResponse simulado = new CalculoResponse(42L, 130.0, 0.13, List.of(
                new ItemDetalhamentoResponse(PaymentType.PIX, 1000L, 0.13, 130.0)), null);
        SimulacaoResponse resposta = new SimulacaoResponse(
                42L, atual, simulado, 850.0, 0.85, 86.73469387755102);

        when(simulacaoService.simular(any())).thenReturn(resposta);

        mockMvc.perform(post("/simulacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.empresaId").value(42))
                .andExpect(jsonPath("$.economiaGramas").value(850.0))
                .andExpect(jsonPath("$.economiaKg").value(0.85))
                .andExpect(jsonPath("$.percentualReducao").value(86.73469387755102))
                .andExpect(jsonPath("$.cenarioAtual.totalEmissoesGramas").value(980.0))
                .andExpect(jsonPath("$.cenarioSimulado.totalEmissoesGramas").value(130.0));
    }

    @Test
    void requestInvalido() throws Exception {
        // empresaId ausente -> @NotNull falha antes do service
        String invalido = """
                {
                  "distribuicaoAtual": [ { "paymentType": "PIX", "quantidade": 10 } ],
                  "distribuicaoSimulada": [ { "paymentType": "PIX", "quantidade": 10 } ]
                }
                """;

        mockMvc.perform(post("/simulacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void distribuicaoSimuladaVaziaRetornaBadRequest() throws Exception {
        String invalido = """
                {
                  "empresaId": 1,
                  "distribuicaoAtual": [ { "paymentType": "PIX", "quantidade": 10 } ],
                  "distribuicaoSimulada": []
                }
                """;

        mockMvc.perform(post("/simulacoes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest());
    }
}
