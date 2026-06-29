package br.com.kaema.ponto.repository;

import br.com.kaema.ponto.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repository da entity User.
 *
 * Ao estender JpaRepository<User, Integer>, ganhamos de graca os metodos
 * basicos (findById, findAll, save, delete, etc.) -- sem escrever SQL.
 * O <User, Integer> diz: "esse repo gerencia a entity User, cuja chave
 * primaria e do tipo Integer".
 *
 * O metodo abaixo e uma "query derivada": o Spring Data LE o nome do metodo
 * e gera o SQL automaticamente. 'findByEmail' vira:
 *   SELECT * FROM users WHERE email = ?
 */
public interface UserRepository extends JpaRepository<User, Integer> {

    // Busca um usuario pelo email. Retorna Optional porque pode nao existir
    // (Optional evita NullPointerException -- e a forma moderna de lidar com
    // "pode ou nao ter resultado").
    Optional<User> findByEmail(String email);
}