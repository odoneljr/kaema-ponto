package br.com.kaema.ponto.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.function.Function;

/**
 * Servico responsavel por tudo relacionado ao token JWT:
 *  - gerar um token a partir do usuario autenticado
 *  - extrair informacoes de um token (ex.: o email)
 *  - validar se um token e legitimo e nao expirou
 *
 * O token e assinado com a chave secreta (jwt.secret, vinda do .env).
 * Quem nao tem a chave nao consegue forjar um token valido.
 */
@Service
public class JwtService {

    // @Value injeta os valores das propriedades (que vem do .env).
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    // Constroi a chave de assinatura a partir do texto secreto.
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // ===== GERAR TOKEN =====
    // Recebe o usuario autenticado e produz o token assinado.
    public String generateToken(UserDetails userDetails) {
        Date agora = new Date();
        Date validade = new Date(agora.getTime() + expiration);

        return Jwts.builder()
                .subject(userDetails.getUsername()) // o "dono" do token = email
                .issuedAt(agora)                    // emitido em
                .expiration(validade)               // expira em
                .signWith(getSigningKey())          // assina com a chave secreta
                .compact();                         // gera a String final
    }

    // ===== EXTRAIR O EMAIL (subject) DO TOKEN =====
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    // ===== EXTRAIR A DATA DE EXPIRACAO =====
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // Metodo generico: extrai um "claim" (informacao) do token.
    private <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = Jwts.parser()
                .verifyWith(getSigningKey())  // confere a assinatura
                .build()
                .parseSignedClaims(token)
                .getPayload();
        return resolver.apply(claims);
    }

    // ===== VALIDAR TOKEN =====
    // Token e valido se: o email bate com o usuario E nao expirou.
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}