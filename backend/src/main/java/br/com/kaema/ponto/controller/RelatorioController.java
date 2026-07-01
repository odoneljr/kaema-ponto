package br.com.kaema.ponto.controller;

import br.com.kaema.ponto.dto.RelatorioDiaResponse;
import br.com.kaema.ponto.entity.WorkingHour;
import br.com.kaema.ponto.repository.WorkingHourRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Relatorio mensal de ponto (Camada 1: visualizacao).
 *
 * Retorna todos os dias do mes selecionado para um funcionario, com as
 * batidas registradas. Dias sem registro vem com as batidas nulas.
 */
@RestController
@RequestMapping("/relatorio")
public class RelatorioController {

    private final WorkingHourRepository workingHourRepository;

    public RelatorioController(WorkingHourRepository workingHourRepository) {
        this.workingHourRepository = workingHourRepository;
    }

    // GET /relatorio?userId=44&ano=2026&mes=6
    @GetMapping
    public List<RelatorioDiaResponse> gerar(
            @RequestParam Integer userId,
            @RequestParam int ano,
            @RequestParam int mes) {

        // Primeiro e ultimo dia do mes.
        YearMonth yearMonth = YearMonth.of(ano, mes);
        LocalDate inicio = yearMonth.atDay(1);
        LocalDate fim = yearMonth.atEndOfMonth();

        // Busca os registros existentes e os indexa por data (para lookup rapido).
        Map<LocalDate, WorkingHour> registrosPorData = workingHourRepository
                .findByUserIdAndWorkDateBetweenOrderByWorkDate(userId, inicio, fim)
                .stream()
                .collect(Collectors.toMap(WorkingHour::getWorkDate, Function.identity()));

        // Gera TODOS os dias do mes; preenche com o registro se existir.
        List<RelatorioDiaResponse> dias = new ArrayList<>();
        for (int dia = 1; dia <= yearMonth.lengthOfMonth(); dia++) {
            LocalDate data = yearMonth.atDay(dia);
            WorkingHour w = registrosPorData.get(data);

            if (w != null) {
                dias.add(new RelatorioDiaResponse(
                        data,
                        w.getTime1(), w.getTime2(), w.getTime3(),
                        w.getTime4(), w.getTime5(), w.getTime6(),
                        w.getObservacao()
                ));
            } else {
                // Dia sem registro: batidas nulas.
                dias.add(new RelatorioDiaResponse(
                        data, null, null, null, null, null, null, null));
            }
        }

        return dias;
    }
}