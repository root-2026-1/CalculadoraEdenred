package com.root.calculadoraedenred.service;

import com.root.calculadoraedenred.dto.CalculoRequest;
import com.root.calculadoraedenred.dto.CalculoResponse;
import com.root.calculadoraedenred.dto.SimulacaoRequest;
import com.root.calculadoraedenred.dto.SimulacaoResponse;
import org.springframework.stereotype.Service;

@Service
public class SimulacaoService {

    private final CalculoEmissoesService calculoEmissoesService;

    public SimulacaoService(CalculoEmissoesService calculoEmissoesService) {
        this.calculoEmissoesService = calculoEmissoesService;
    }

    public SimulacaoResponse simular(SimulacaoRequest request) {
        CalculoResponse atual = calculoEmissoesService.calcular(new CalculoRequest(
                request.getEmpresaId(), request.getDistribuicaoAtual()));
        CalculoResponse simulado = calculoEmissoesService.calcular(new CalculoRequest(
                request.getEmpresaId(), request.getDistribuicaoSimulada()));

        double economiaGramas = atual.getTotalEmissoesGramas() - simulado.getTotalEmissoesGramas();
        double percentualReducao = atual.getTotalEmissoesGramas() > 0
                ? (economiaGramas / atual.getTotalEmissoesGramas()) * 100.0
                : 0.0;

        return new SimulacaoResponse(
                request.getEmpresaId(),
                atual,
                simulado,
                economiaGramas,
                economiaGramas / 1000.0,
                percentualReducao
        );
    }
}
