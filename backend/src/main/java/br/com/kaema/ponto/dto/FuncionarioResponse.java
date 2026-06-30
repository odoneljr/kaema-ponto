package br.com.kaema.ponto.dto;

/**
 * Dados de um funcionario expostos pela API (sem campos sensiveis
 * como senha, cpf, rg). Usado na listagem de funcionarios.
 */
public record FuncionarioResponse(
        Integer id,
        String name,
        String email,
        Boolean isAdmin,
        String tagRfid
) {
}