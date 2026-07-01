package br.com.kaema.ponto.controller;

import br.com.kaema.ponto.dto.FuncionarioResponse;
import br.com.kaema.ponto.repository.UserRepository;
import br.com.kaema.ponto.repository.WorkingHourRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

/**
 * Endpoint dos faltosos do dia: funcionarios ativos (sem end_date)
 * que ainda nao registraram ponto hoje.
 */
@RestController
@RequestMapping("/faltosos")
public class FaltososController {

    private final UserRepository userRepository;
    private final WorkingHourRepository workingHourRepository;

    public FaltososController(UserRepository userRepository,
                              WorkingHourRepository workingHourRepository) {
        this.userRepository = userRepository;
        this.workingHourRepository = workingHourRepository;
    }

    // GET /faltosos -> lista os faltosos de hoje.
    @GetMapping
    public List<FuncionarioResponse> faltososHoje() {
        LocalDate hoje = LocalDate.now();

        // IDs de quem JA bateu ponto hoje.
        List<Integer> comPonto = workingHourRepository.findUserIdsComPontoNaData(hoje);

        // Funcionarios ativos que NAO estao na lista de "com ponto".
        return userRepository.findByEndDateIsNull().stream()
                .filter(user -> !comPonto.contains(user.getId()))
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