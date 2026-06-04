package com.root.calculadoraedenred.controller;

import com.root.calculadoraedenred.dto.CalculoRequest;
import com.root.calculadoraedenred.exception.GlobalExceptionHandler;
import com.root.calculadoraedenred.exception.RelatorioGeracaoException;
import com.root.calculadoraedenred.service.CalculoEmissoesService;
import com.root.calculadoraedenred.service.RelatorioExportacaoService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.mockito.ArgumentCaptor;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CalculoController.class)
@Import(GlobalExceptionHandler.class)
class CalculoControllerExportacaoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RelatorioExportacaoService exportacaoService;

    @MockBean
    private CalculoEmissoesService calculoEmissoesService;

    private static final String REQUEST_JSON = """
            {
              "empresaId": 42,
              "itens": [
                { "paymentType": "PIX", "quantidade": 100 },
                { "paymentType": "PHYSICAL", "quantidade": 50 }
              ]
            }
            """;

    @Test
    void retornaPdf() throws Exception {
        byte[] pdfFake = "%PDF-1.4 conteudo".getBytes();
        when(exportacaoService.exportarPdf(any())).thenReturn(pdfFake);

        mockMvc.perform(post("/calculos/exportar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_PDF))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
                        org.hamcrest.Matchers.containsString(
                                "attachment; filename=\"relatorio-emissoes-empresa-42.pdf\"")))
                .andExpect(content().bytes(pdfFake));
    }

    @Test
    void requestInvalido() throws Exception {
        // empresaId ausente -> validação @NotNull falha antes de chamar o service
        String invalido = """
                { "itens": [ { "paymentType": "PIX", "quantidade": 10 } ] }
                """;

        mockMvc.perform(post("/calculos/exportar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalido))
                .andExpect(status().isBadRequest());
    }

    @Test
    void periodoReferenciaEPassadoAoService() throws Exception {
        ArgumentCaptor<CalculoRequest> captor = ArgumentCaptor.forClass(CalculoRequest.class);
        when(exportacaoService.exportarPdf(captor.capture())).thenReturn("%PDF".getBytes());

        mockMvc.perform(post("/calculos/exportar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "empresaId": 1,
                                  "periodoReferencia": "Último mês (2026-06-01 a 2026-06-04)",
                                  "itens": [{ "paymentType": "PIX", "quantidade": 10 }]
                                }"""))
                .andExpect(status().isOk());

        assertEquals("Último mês (2026-06-01 a 2026-06-04)",
                captor.getValue().getPeriodoReferencia());
    }

    @Test
    void retornaErroAoFalharGeracaoPdf() throws Exception {
        when(exportacaoService.exportarPdf(any()))
                .thenThrow(new RelatorioGeracaoException("falha"));

        mockMvc.perform(post("/calculos/exportar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(REQUEST_JSON))
                .andExpect(status().isInternalServerError())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message")
                        .value("Não foi possível gerar o relatório. Tente novamente."));
    }
}
