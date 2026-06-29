package br.com.kaema.ponto.dto;

/**
 * Dados que chegam na requisicao de login (o que o frontend envia).
 * 'record' e uma forma concisa do Java moderno de criar uma classe
 * imutavel de dados (gera construtor, getters, etc. automaticamente).
 */
public record LoginRequest(String email, String password) {
}