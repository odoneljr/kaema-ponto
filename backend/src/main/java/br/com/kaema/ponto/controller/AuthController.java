package br.com.kaema.ponto.controller;

import br.com.kaema.ponto.dto.LoginRequest;
import br.com.kaema.ponto.dto.LoginResponse;
import br.com.kaema.ponto.security.JwtService;
import br.com.kaema.ponto.security.UserDetailsImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller de autenticacao. Expoe o endpoint POST /auth/login.
 * Recebe email+senha, valida via AuthenticationManager, e devolve o JWT.
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthController(AuthenticationManager authenticationManager,
                          JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // Dispara a autenticacao: o Spring confere email+senha no banco
        // (via UserDetailsService + BCrypt). Se falhar, lanca excecao.
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(), request.password())
        );

        // Se chegou aqui, as credenciais sao validas. Pega o usuario.
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        // Gera o token e monta a resposta.
        String token = jwtService.generateToken(userDetails);

        return new LoginResponse(
                token,
                userDetails.getUser().getName(),
                userDetails.getUser().getEmail(),
                Boolean.TRUE.equals(userDetails.getUser().getIsAdmin())
        );
    }
}