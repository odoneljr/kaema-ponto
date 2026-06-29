package br.com.kaema.ponto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity que espelha a tabela 'hours_adjustments' (ajustes de banco de horas).
 * Guarda, por usuario e por mes, os segundos a somar (positivo) ou
 * descontar (negativo) do saldo de horas.
 *
 * Conceitos novos:
 *  - UNIQUE composta (user_id + year_month): um registro por usuario/mes.
 *  - Colunas de timestamp gerenciadas pelo BANCO (created_at / updated_at):
 *    marcamos como nao-inserir/nao-atualizar para o Hibernate nao mexer nelas.
 */
@Entity
@Table(
        name = "hours_adjustments",
        uniqueConstraints = @UniqueConstraint(
                name = "uq_user_month",
                columnNames = {"user_id", "year_month"}
        )
)
@Getter
@Setter
public class HoursAdjustment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // int unsigned NOT NULL — o id do usuario (por ora, apenas o numero;
    // futuramente pode virar um relacionamento @ManyToOne com User).
    @Column(name = "user_id", nullable = false)
    private Integer userId;

    // char(7) NOT NULL — o periodo no formato "AAAA-MM" (ex.: "2026-06")
    @Column(name = "year_month", nullable = false, length = 7, columnDefinition = "char(7)")
    private String yearMonth;

    // int NOT NULL DEFAULT 0 — segundos a SOMAR no saldo
    @Column(name = "pos_adjust_secs", nullable = false)
    private Integer posAdjustSecs;

    // int NOT NULL DEFAULT 0 — segundos a DESCONTAR do saldo
    @Column(name = "neg_adjust_secs", nullable = false)
    private Integer negAdjustSecs;

    // datetime gerenciado pelo banco (DEFAULT current_timestamp).
    // insertable=false, updatable=false: o Hibernate LE, mas nunca escreve.
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    // datetime gerenciado pelo banco (ON UPDATE current_timestamp).
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}