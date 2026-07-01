package br.com.kaema.ponto.dto;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Representa UM dia no relatorio mensal de ponto.
 * As batidas (time1..time6) podem ser nulas se nao houve registro.
 */
public record RelatorioDiaResponse(
        LocalDate data,
        LocalTime time1,
        LocalTime time2,
        LocalTime time3,
        LocalTime time4,
        LocalTime time5,
        LocalTime time6,
        String observacao
) {
}