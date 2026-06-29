package br.com.kaema.ponto.security;

import br.com.kaema.ponto.entity.User;
import br.com.kaema.ponto.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Implementa o UserDetailsService do Spring Security.
 * Sua unica tarefa: dado um email, buscar o User no banco e entrega-lo
 * embrulhado em UserDetailsImpl. O Security usa isso durante o login.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    // Injecao de dependencia: o Spring fornece o UserRepository pronto.
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Chamado pelo Security durante a autenticacao.
    // Apesar do nome 'loadUserByUsername', nosso "username" e o email.
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuario nao encontrado com email: " + email));
        return new UserDetailsImpl(user);
    }
}