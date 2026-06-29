package br.com.kaema.ponto.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro que roda UMA vez por requisicao (OncePerRequestFilter).
 * Sua tarefa: ler o token JWT do cabecalho Authorization, valida-lo,
 * e se for legitimo, autenticar o usuario no contexto do Spring Security.
 *
 * Fluxo:
 *   1. Pega o cabecalho "Authorization: Bearer <token>"
 *   2. Extrai o email do token
 *   3. Carrega o usuario e valida o token
 *   4. Se valido, marca a requisicao como autenticada
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService,
                                   UserDetailsServiceImpl userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");

        // Sem cabecalho ou fora do formato "Bearer ..." -> segue sem autenticar.
        // (Se a rota for protegida, o SecurityConfig barra mais adiante.)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Remove o prefixo "Bearer " (7 caracteres) e fica so com o token.
        final String token = authHeader.substring(7);
        final String email = jwtService.extractUsername(token);

        // So autentica se: ha email no token E ninguem ainda foi autenticado
        // nesta requisicao.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            if (jwtService.isTokenValid(token, userDetails)) {
                // Cria o "cracha" de autenticacao e o coloca no contexto.
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continua a cadeia de filtros (deixa a requisicao seguir).
        filterChain.doFilter(request, response);
    }
}