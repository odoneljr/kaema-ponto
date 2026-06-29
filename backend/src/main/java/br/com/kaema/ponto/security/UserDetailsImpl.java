package br.com.kaema.ponto.security;

import br.com.kaema.ponto.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * "Adaptador" entre a nossa entity User e o que o Spring Security entende
 * como usuario autenticavel (a interface UserDetails).
 *
 * O Security nao conhece nossa tabela 'users'. Esta classe traduz:
 *   - getUsername() -> usamos o EMAIL como identificador de login
 *   - getPassword() -> o hash bcrypt vindo do banco
 *   - getAuthorities() -> os "papeis" (ROLE_ADMIN ou ROLE_USER) com base no is_admin
 */
public class UserDetailsImpl implements UserDetails {

    private final User user;

    public UserDetailsImpl(User user) {
        this.user = user;
    }

    // Expoe a entity original, caso precisemos do id/nome depois do login.
    public User getUser() {
        return user;
    }

    // O "papel" do usuario. Se is_admin = true -> ROLE_ADMIN, senao ROLE_USER.
    // Os papeis controlam quem acessa o que (autorizacao).
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        String role = Boolean.TRUE.equals(user.getIsAdmin()) ? "ROLE_ADMIN" : "ROLE_USER";
        return List.of(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return user.getPassword(); // hash bcrypt do banco
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // login e por email
    }

    // Os 4 metodos abaixo controlam estados da conta. Retornamos 'true'
    // (conta sempre valida) por enquanto. Futuramente, o 'end_date' do
    // funcionario poderia desativar a conta aqui.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}