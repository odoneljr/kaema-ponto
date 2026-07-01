package br.com.kaema.ponto.repository;

import br.com.kaema.ponto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    // NOVO: busca um funcionario pela tag RFID vinculada.
    // Gera: SELECT * FROM users WHERE tag_rfid = ?
    Optional<User> findByTagRfid(String tagRfid);

    // Funcionarios ativos = sem data de desligamento (end_date NULL).
    java.util.List<User> findByEndDateIsNull();
}