package br.com.kaema.ponto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Entity que espelha a tabela 'working_hours' (ponto diario).
 * Um registro = um dia de trabalho de um funcionario, com ate 6 batidas
 * (time1..time6) e o total de segundos trabalhados (worked_time).
 *
 * Conceito novo e principal: RELACIONAMENTO @ManyToOne com User.
 * No banco, 'user_id' e uma FK para users(id). Em vez de mapear como
 * numero cru, mapeamos como o OBJETO User de verdade -- assim, dado um
 * WorkingHour, voce acessa workingHour.getUser().getName() diretamente.
 */
@Entity
@Table(
        name = "working_hours",
        uniqueConstraints = @UniqueConstraint(
                name = "cons_user_day",
                columnNames = {"user_id", "work_date"}
        )
)
@Getter
@Setter
public class WorkingHour {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // RELACIONAMENTO: muitos WorkingHour pertencem a um User (Many-To-One).
    // 'fetch = LAZY' = so carrega o User do banco quando voce realmente
    // acessar (mais eficiente). @JoinColumn diz qual e a coluna FK.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // date NOT NULL — o dia do registro
    @Column(name = "work_date", nullable = false)
    private LocalDate workDate;

    // time — as 6 possiveis batidas do dia. 'time' do SQL vira LocalTime.
    @Column(name = "time1")
    private LocalTime time1;

    @Column(name = "time2")
    private LocalTime time2;

    @Column(name = "time3")
    private LocalTime time3;

    @Column(name = "time4")
    private LocalTime time4;

    @Column(name = "time5")
    private LocalTime time5;

    @Column(name = "time6")
    private LocalTime time6;

    // int — total de segundos trabalhados no dia (pode ser nulo)
    @Column(name = "worked_time")
    private Integer workedTime;

    // varchar(255) — observacao livre
    @Column(name = "observacao", length = 255)
    private String observacao;

    // varchar(100) NOT NULL DEFAULT '' — a tag que registrou
    @Column(name = "tag_rfid", nullable = false, length = 100)
    private String tagRfid;
}