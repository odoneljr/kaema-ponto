package br.com.kaema.ponto.dto;

/**
 * Dados de um evento de leitura de tag, enviado ao frontend via SSE.
 * O frontend usa 'tipo' para decidir qual pop-up mostrar.
 *
 *  tipo = "VINCULADA"     -> mostra dados do funcionario + horario batido
 *  tipo = "NAO_VINCULADA" -> mostra "tag nao vinculada" + botao cadastrar
 */
public record LeituraEvento(
        String tipo,        // "VINCULADA" ou "NAO_VINCULADA"
        String codigoTag,   // o codigo da tag lida
        String nomeFuncionario, // null se nao vinculada
        Integer funcionarioId,  // null se nao vinculada
        String horario          // hora da batida (null se nao vinculada)
) {
}