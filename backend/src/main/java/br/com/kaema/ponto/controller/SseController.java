package br.com.kaema.ponto.controller;

import br.com.kaema.ponto.service.SseService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * Controller que expoe o canal SSE para o frontend.
 *
 * O navegador faz GET em /eventos/leituras e a conexao fica ABERTA,
 * recebendo eventos empurrados pelo backend (ex.: leitura de tag).
 */
@RestController
@RequestMapping("/eventos")
public class SseController {

    private final SseService sseService;

    public SseController(SseService sseService) {
        this.sseService = sseService;
    }

    // produces = text/event-stream: o tipo MIME do SSE. Diz ao navegador
    // "isto e um fluxo de eventos, mantenha a conexao aberta".
    @GetMapping(value = "/leituras", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter assinarLeituras() {
        return sseService.criarConexao();
    }
}