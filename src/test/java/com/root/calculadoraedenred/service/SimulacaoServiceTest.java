package com.root.calculadoraedenred.service;

import com.root.calculadoraedenred.dto.CalculoRequest;
import com.root.calculadoraedenred.dto.CalculoResponse;
import com.root.calculadoraedenred.dto.ItemCalculoRequest;
import com.root.calculadoraedenred.dto.SimulacaoRequest;
import com.root.calculadoraedenred.dto.SimulacaoResponse;
import com.root.calculadoraedenred.model.enums.PaymentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatcher;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.offset;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SimulacaoServiceTest {

    private CalculoEmissoesService calculoEmissoesService;
    private SimulacaoService simulacaoService;

    @BeforeEach
    void setUp() {
        calculoEmissoesService = mock(CalculoEmissoesService.class);
        simulacaoService = new SimulacaoService(calculoEmissoesService);
    }

    @Test
    void calculaEconomiaQuandoCenarioSimuladoEhMaisLimpo() {
        SimulacaoRequest request = new SimulacaoRequest(
                42L,
                List.of(new ItemCalculoRequest(PaymentType.PHYSICAL, 1000L)),
                List.of(new ItemCalculoRequest(PaymentType.PIX, 1000L))
        );

        when(calculoEmissoesService.calcular(argThat(empresaIdAndPaymentTypes(42L, PaymentType.PHYSICAL))))
                .thenReturn(new CalculoResponse(42L, 980.0, 0.98, List.of(), null));
        when(calculoEmissoesService.calcular(argThat(empresaIdAndPaymentTypes(42L, PaymentType.PIX))))
                .thenReturn(new CalculoResponse(42L, 130.0, 0.13, List.of(), null));

        SimulacaoResponse resposta = simulacaoService.simular(request);

        assertThat(resposta.getEmpresaId()).isEqualTo(42L);
        assertThat(resposta.getEconomiaGramas()).isEqualTo(850.0);
        assertThat(resposta.getEconomiaKg()).isEqualTo(0.85);
        assertThat(resposta.getPercentualReducao()).isCloseTo(86.7347, offset(0.0001));
        assertThat(resposta.getCenarioAtual().getTotalEmissoesGramas()).isEqualTo(980.0);
        assertThat(resposta.getCenarioSimulado().getTotalEmissoesGramas()).isEqualTo(130.0);
    }

    @Test
    void retornaPercentualZeroQuandoEmissoesAtuaisSaoZero() {
        SimulacaoRequest request = new SimulacaoRequest(
                1L,
                List.of(new ItemCalculoRequest(PaymentType.PIX, 0L)),
                List.of(new ItemCalculoRequest(PaymentType.PIX, 0L))
        );

        when(calculoEmissoesService.calcular(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new CalculoResponse(1L, 0.0, 0.0, List.of(), null));

        SimulacaoResponse resposta = simulacaoService.simular(request);

        assertThat(resposta.getEconomiaGramas()).isZero();
        assertThat(resposta.getEconomiaKg()).isZero();
        assertThat(resposta.getPercentualReducao()).isZero();
    }

    @Test
    void economiaPodeSerNegativaQuandoCenarioSimuladoPiora() {
        SimulacaoRequest request = new SimulacaoRequest(
                3L,
                List.of(new ItemCalculoRequest(PaymentType.PIX, 1000L)),
                List.of(new ItemCalculoRequest(PaymentType.PHYSICAL, 1000L))
        );

        when(calculoEmissoesService.calcular(argThat(empresaIdAndPaymentTypes(3L, PaymentType.PIX))))
                .thenReturn(new CalculoResponse(3L, 130.0, 0.13, List.of(), null));
        when(calculoEmissoesService.calcular(argThat(empresaIdAndPaymentTypes(3L, PaymentType.PHYSICAL))))
                .thenReturn(new CalculoResponse(3L, 980.0, 0.98, List.of(), null));

        SimulacaoResponse resposta = simulacaoService.simular(request);

        assertThat(resposta.getEconomiaGramas()).isEqualTo(-850.0);
        assertThat(resposta.getPercentualReducao()).isNegative();
    }

    @Test
    void delegaCalculosComEmpresaIdENaoMisturaDistribuicoes() {
        SimulacaoRequest request = new SimulacaoRequest(
                99L,
                List.of(new ItemCalculoRequest(PaymentType.PHYSICAL, 100L)),
                List.of(new ItemCalculoRequest(PaymentType.PIX, 100L))
        );

        when(calculoEmissoesService.calcular(org.mockito.ArgumentMatchers.any()))
                .thenReturn(new CalculoResponse(99L, 0.0, 0.0, List.of(), null));

        simulacaoService.simular(request);

        verify(calculoEmissoesService).calcular(argThat(empresaIdAndPaymentTypes(99L, PaymentType.PHYSICAL)));
        verify(calculoEmissoesService).calcular(argThat(empresaIdAndPaymentTypes(99L, PaymentType.PIX)));
    }

    private ArgumentMatcher<CalculoRequest> empresaIdAndPaymentTypes(Long empresaId, PaymentType... tipos) {
        return req -> req != null
                && empresaId.equals(req.getEmpresaId())
                && req.getItens() != null
                && req.getItens().size() == tipos.length
                && java.util.stream.IntStream.range(0, tipos.length)
                        .allMatch(i -> req.getItens().get(i).getPaymentType() == tipos[i]);
    }
}
