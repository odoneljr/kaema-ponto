package br.com.kaema.ponto.controller;

import br.com.kaema.ponto.dto.FuncionarioResponse;
import br.com.kaema.ponto.repository.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Endpoints de gestao de funcionarios.
 *
 * IMPORTANTE (pendencia): estes endpoints sao de uso ADMIN. Ainda falta
 * proteger por perfil (ROLE_ADMIN) para que usuarios comuns nao acessem.
 */
@RestController
@RequestMapping("/funcionarios")
public class FuncionarioController {

    private final UserRepository userRepository;

    public FuncionarioController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // GET /funcionarios -> lista todos os funcionarios.
    @GetMapping
    public List<FuncionarioResponse> listar() {
        return userRepository.findAll().stream()
                .map(user -> new FuncionarioResponse(
                        user.getId(),
                        user.getName(),
                        user.getEmail(),
                        user.getIsAdmin(),
                        user.getTagRfid()
                ))
                .toList();
    }
}