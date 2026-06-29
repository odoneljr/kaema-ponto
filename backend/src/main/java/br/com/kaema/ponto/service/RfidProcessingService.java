package br.com.kaema.ponto.service;

import br.com.kaema.ponto.entity.RfidCode;
import br.com.kaema.ponto.entity.User;
import br.com.kaema.ponto.entity.WorkingHour;
import br.com.kaema.ponto.repository.RfidCodeRepository;
import br.com.kaema.ponto.repository.UserRepository;
import br.com.kaema.ponto.repository.WorkingHourRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

/**
 * Detecta leituras de tag do ESP e registra o ponto em working_hours.
 *
 * Regras:
 *  - Tag vinculada -> registra batida no proximo slot livre (time1..time6).
 *  - Primeira batida do dia -> cria o registro com time1.
 *  - Debounce -> ignora batida se a anterior foi ha menos de N minutos.
 *  - Tag nao vinculada -> apenas sinaliza (SSE/pop-up vira depois).
 */
@Service
public class RfidProcessingService {

    private static final Logger log = LoggerFactory.getLogger(RfidProcessingService.class);

    private final RfidCodeRepository rfidCodeRepository;
    private final UserRepository userRepository;
    private final WorkingHourRepository workingHourRepository;

    // Tempo minimo entre batidas (vem do application.properties / .env).
    @Value("${ponto.debounce-minutos}")
    private int debounceMinutos;

    public RfidProcessingService(RfidCodeRepository rfidCodeRepository,
                                 UserRepository userRepository,
                                 WorkingHourRepository workingHourRepository) {
        this.rfidCodeRepository = rfidCodeRepository;
        this.userRepository = userRepository;
        this.workingHourRepository = workingHourRepository;
    }

    @Scheduled(fixedDelay = 2000)
    public void processarLeiturasPendentes() {
        List<RfidCode> pendentes =
                rfidCodeRepository.findByStatusOrderByDataHoraAsc("pendente");

        if (pendentes.isEmpty()) {
            return;
        }

        for (RfidCode leitura : pendentes) {
            processarUma(leitura);
            leitura.setStatus("processado");
            rfidCodeRepository.save(leitura);
        }
    }

    private void processarUma(RfidCode leitura) {
        String codigoTag = leitura.getCodigo();
        Optional<User> funcionarioOpt = userRepository.findByTagRfid(codigoTag);

        if (funcionarioOpt.isEmpty()) {
            log.warn("Tag {} NAO VINCULADA a nenhum funcionario.", codigoTag);
            // TODO: SSE -> pop-up "cadastrar usuario"
            return;
        }

        User funcionario = funcionarioOpt.get();
        registrarPonto(funcionario, leitura);
    }

    private void registrarPonto(User funcionario, RfidCode leitura) {
        LocalDate hoje = leitura.getDataHora().toLocalDate();
        LocalTime horaBatida = leitura.getDataHora().toLocalTime();

        // Busca (ou prepara) o registro de hoje.
        WorkingHour registro = workingHourRepository
                .findByUserAndWorkDate(funcionario, hoje)
                .orElseGet(() -> {
                    WorkingHour novo = new WorkingHour();
                    novo.setUser(funcionario);
                    novo.setWorkDate(hoje);
                    novo.setTagRfid(leitura.getCodigo());
                    return novo;
                });

        // DEBOUNCE: se a ultima batida foi ha menos de N minutos, ignora.
        LocalTime ultima = ultimaBatida(registro);
        if (ultima != null
                && ultima.plusMinutes(debounceMinutos).isAfter(horaBatida)) {
            log.info("Batida de {} IGNORADA (debounce de {} min).",
                    funcionario.getName(), debounceMinutos);
            return;
        }

        // Acha o proximo slot vazio e registra.
        boolean registrou = preencherProximoSlot(registro, horaBatida);

        if (!registrou) {
            log.warn("Funcionario {} ja tem as 6 batidas do dia preenchidas.",
                    funcionario.getName());
            return;
        }

        workingHourRepository.save(registro);
        log.info("Ponto registrado: {} as {}", funcionario.getName(), horaBatida);
        // TODO: SSE -> pop-up com dados do funcionario + horario
    }

    // Retorna a hora do ultimo slot preenchido (ou null se nenhum).
    private LocalTime ultimaBatida(WorkingHour r) {
        if (r.getTime6() != null) return r.getTime6();
        if (r.getTime5() != null) return r.getTime5();
        if (r.getTime4() != null) return r.getTime4();
        if (r.getTime3() != null) return r.getTime3();
        if (r.getTime2() != null) return r.getTime2();
        if (r.getTime1() != null) return r.getTime1();
        return null;
    }

    // Preenche o primeiro slot vazio. Retorna false se todos estao cheios.
    private boolean preencherProximoSlot(WorkingHour r, LocalTime hora) {
        if (r.getTime1() == null) { r.setTime1(hora); return true; }
        if (r.getTime2() == null) { r.setTime2(hora); return true; }
        if (r.getTime3() == null) { r.setTime3(hora); return true; }
        if (r.getTime4() == null) { r.setTime4(hora); return true; }
        if (r.getTime5() == null) { r.setTime5(hora); return true; }
        if (r.getTime6() == null) { r.setTime6(hora); return true; }
        return false;
    }
}