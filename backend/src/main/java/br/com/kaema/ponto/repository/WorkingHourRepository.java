package br.com.kaema.ponto.repository;

import br.com.kaema.ponto.entity.User;
import br.com.kaema.ponto.entity.WorkingHour;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

/**
 * Repository da entity WorkingHour (tabela 'working_hours' - ponto diario).
 */
public interface WorkingHourRepository extends JpaRepository<WorkingHour, Integer> {

    // Busca o registro de ponto de um funcionario em uma data especifica.
    // Lembra da constraint UNIQUE (user_id, work_date): existe no maximo
    // UM registro por funcionario por dia. Por isso retorna Optional (um so).
    // Gera: SELECT * FROM working_hours WHERE user_id = ? AND work_date = ?
    Optional<WorkingHour> findByUserAndWorkDate(User user, LocalDate workDate);
}