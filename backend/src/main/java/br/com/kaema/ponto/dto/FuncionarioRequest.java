package br.com.kaema.ponto.dto;

import java.time.LocalDate;

/**
 * Dados recebidos do frontend para cadastrar um funcionario.
 * A senha vem em texto puro e sera criptografada (BCrypt) antes de salvar.
 */
public record FuncionarioRequest(
        String name,
        String email,
        String password,
        LocalDate startDate,   // data de admissao
        String cpf,
        String rg,
        String ctps,
        String pispasep,
        String cargo,
        String gestor,
        String centrocusto,
        String tagRfid,        // opcional
        Boolean isAdmin        // opcional (default false)
) {
}