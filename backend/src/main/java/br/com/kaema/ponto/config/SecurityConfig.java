package br.com.kaema.ponto.config;

import br.com.kaema.ponto.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

/**
 * Configuracao central do Spring Security.
 * Define: quais rotas sao publicas, como as senhas sao verificadas (BCrypt),
 * e registra o nosso filtro JWT no fluxo de cada requisicao.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter,
                          UserDetailsService userDetailsService) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.userDetailsService = userDetailsService;
    }

    // Define a "corrente de seguranca": as regras de acesso da aplicacao.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desativa CSRF: nao usamos sessao/cookies, e sim token. CSRF e
                // protecao para apps baseados em sessao; aqui nao se aplica.
                .csrf(AbstractHttpConfigurer::disable)

                // Define quais rotas sao livres e quais exigem autenticacao.
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .requestMatchers("/eventos/**").permitAll()
                        .anyRequest().authenticated()
                )

                // STATELESS: o servidor nao guarda sessao. Cada requisicao se
                // autentica sozinha pelo token. Essencial para API + JWT.
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )

                // Registra o provider que sabe verificar usuario+senha no banco.
                .authenticationProvider(authenticationProvider())

                // Coloca nosso filtro JWT ANTES do filtro padrao de login.
                // Assim, todo request passa primeiro pela validacao do token.
                .addFilterBefore(jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    // BCrypt: o algoritmo que compara a senha digitada com o hash do banco.
    // O mesmo usado pelo PHP legado (password_hash) -> compatibilidade total.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // O provider liga o UserDetailsService (carrega usuario) ao PasswordEncoder
    // (compara senha). E o mecanismo que valida credenciais no login.
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    // Expoe o AuthenticationManager, que o controller de login vai usar
    // para disparar a autenticacao.
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}