package br.com.kaema.ponto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity que espelha a tabela 'users' do banco.
 * Versao MINIMA: mapeia apenas as colunas essenciais para autenticacao
 * e vinculo de tag. As demais colunas (cpf, jornada, etc.) serao
 * adicionadas em etapas posteriores.
 *
 * IMPORTANTE: esta tabela acumula DUAS responsabilidades no sistema legado:
 *  - credencial de acesso (email + password + is_admin)
 *  - dados do funcionario (nome, tag, etc.)
 * Mantemos assim para nao quebrar a producao.
 */
@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    // Chave primaria. IDENTITY = o banco gera o valor (AUTO_INCREMENT).
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // varchar(100) NOT NULL
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    // varchar(50) NOT NULL, UNIQUE — usado no login
    @Column(name = "email", nullable = false, length = 50, unique = true)
    private String email;

    // varchar(255) NOT NULL — hash bcrypt da senha
    @Column(name = "password", nullable = false, length = 255)
    private String password;

    // tinyint(1) — no Java tratamos como boolean (true = administrador)
    // O nome no banco e 'is_admin'; no Java usamos 'isAdmin'.
    @Column(name = "is_admin", nullable = false)
    private Boolean isAdmin;

    // varchar(255), pode ser nulo, UNIQUE — a tag RFID vinculada ao funcionario
    @Column(name = "tag_rfid", length = 255, unique = true)
    private String tagRfid;
}