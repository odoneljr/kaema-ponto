package br.com.kaema.ponto.dto;

/**
 * Resposta do login: o token JWT e alguns dados basicos do usuario,
 * para o frontend exibir/usar (sem nunca devolver a senha).
 */
public record LoginResponse(String token, String name, String email, boolean isAdmin) {
}