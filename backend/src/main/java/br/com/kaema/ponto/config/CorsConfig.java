package br.com.kaema.ponto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.List;

/**
 * Configuracao de CORS (Cross-Origin Resource Sharing).
 *
 * Autoriza o frontend (React, em localhost:5173) a fazer requisicoes
 * a esta API (localhost:8080). Sem isso, o navegador bloqueia as chamadas
 * por seguranca.
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();

        // Origens autorizadas (o endereco do frontend em desenvolvimento).
        config.setAllowedOrigins(List.of("http://localhost:5173"));

        // Metodos HTTP permitidos.
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Cabecalhos permitidos (Authorization para o token, Content-Type, etc.).
        config.setAllowedHeaders(List.of("*"));

        // Permite o envio de credenciais (necessario para o token).
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // aplica a todas as rotas

        return new CorsFilter(source);
    }
}