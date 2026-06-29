package br.com.kaema.ponto.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Gerencia as conexoes SSE (Server-Sent Events) abertas com os navegadores.
 *
 * SSE e um canal de mao unica: servidor -> navegador. O navegador "assina"
 * uma vez, a conexao fica aberta, e o servidor empurra eventos quando quiser.
 *
 * Esta classe:
 *  - registra novas conexoes (quando uma tela abre o canal)
 *  - remove conexoes que caem
 *  - envia (empurra) um evento para TODOS os conectados
 */
@Service
public class SseService {

    private static final Logger log = LoggerFactory.getLogger(SseService.class);

    // Lista thread-safe das conexoes ativas. CopyOnWriteArrayList e seguro
    // para acesso concorrente (varias threads lendo/escrevendo).
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    // Chamado quando uma tela (navegador) abre o canal de eventos.
    public SseEmitter criarConexao() {
        // timeout longo (Long.MAX_VALUE = praticamente nunca expira por tempo).
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.add(emitter);
        log.info("Nova conexao SSE. Total de conexoes: {}", emitters.size());

        // Limpeza: se a conexao terminar ou der erro, remove da lista.
        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));
        emitter.onError(e -> emitters.remove(emitter));

        return emitter;
    }

    // Empurra um evento (com um nome e um dado) para TODOS os conectados.
    public void enviarEvento(String nomeEvento, Object dado) {
        for (SseEmitter emitter : emitters) {
            try {
                emitter.send(SseEmitter.event()
                        .name(nomeEvento)
                        .data(dado));
            } catch (IOException e) {
                // Conexao caiu: remove da lista.
                emitters.remove(emitter);
            }
        }
    }
}