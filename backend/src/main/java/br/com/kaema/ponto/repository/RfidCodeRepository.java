package br.com.kaema.ponto.repository;

import br.com.kaema.ponto.entity.RfidCode;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository da entity RfidCode (tabela 'codigos_rfid', onde o ESP grava).
 *
 * Usamos para LER as leituras pendentes e, depois, ATUALIZAR o status
 * para 'processado'. Nunca alteramos a estrutura da tabela.
 */
public interface RfidCodeRepository extends JpaRepository<RfidCode, Integer> {

    // Query derivada: busca todas as leituras com determinado status,
    // ordenadas da mais antiga para a mais recente (ordem de chegada).
    // Gera: SELECT * FROM codigos_rfid WHERE status = ? ORDER BY data_hora ASC
    List<RfidCode> findByStatusOrderByDataHoraAsc(String status);
}