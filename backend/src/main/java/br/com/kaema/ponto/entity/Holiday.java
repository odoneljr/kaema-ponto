package br.com.kaema.ponto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * Entity que espelha a tabela 'holidays' (feriados).
 * Caso especial: NAO tem coluna 'id'. A propria DATA do feriado
 * e a chave primaria (faz sentido: nao existe o mesmo feriado duas vezes
 * na mesma data).
 */
@Entity
@Table(name = "holidays")
@Getter
@Setter
public class Holiday {

    // A PK e a propria data. Sem @GeneratedValue: nao e auto-incremento,
    // o valor vem de fora (voce define a data do feriado).
    // 'date' do SQL vira LocalDate no Java (tipo moderno de data sem hora).
    @Id
    @Column(name = "holiday_date")
    private LocalDate holidayDate;

    // varchar(100) NOT NULL — nome do feriado (ex.: "Natal")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
}