package br.com.kaema.ponto.controller;

import br.com.kaema.ponto.dto.FuncionarioRequest;
import br.com.kaema.ponto.dto.FuncionarioResponse;
import br.com.kaema.ponto.entity.User;
import br.com.kaema.ponto.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

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
    private final PasswordEncoder passwordEncoder;

    public FuncionarioController(UserRepository userRepository,
                                 PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    // POST /funcionarios -> cadastra um novo funcionario.
    @PostMapping
    public FuncionarioResponse cadastrar(@RequestBody FuncionarioRequest req) {
        User user = new User();
        user.setName(req.name());
        user.setEmail(req.email());
        // Criptografa a senha com BCrypt antes de salvar.
        user.setPassword(passwordEncoder.encode(req.password()));
        user.setStartDate(req.startDate());
        user.setCpf(req.cpf());
        user.setRg(req.rg());
        user.setCtps(req.ctps());
        user.setPispasep(req.pispasep());
        user.setCargo(req.cargo());
        user.setGestor(req.gestor());
        user.setCentrocusto(req.centrocusto());
        // Tag e opcional (pode ser null).
        user.setTagRfid(req.tagRfid());
        // is_admin: usa o valor enviado, ou false se nao vier.
        user.setIsAdmin(req.isAdmin() != null ? req.isAdmin() : false);

        User salvo = userRepository.save(user);

        return new FuncionarioResponse(
                salvo.getId(),
                salvo.getName(),
                salvo.getEmail(),
                salvo.getIsAdmin(),
                salvo.getTagRfid()
        );
    }
}