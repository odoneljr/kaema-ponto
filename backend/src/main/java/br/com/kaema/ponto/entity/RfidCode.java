package br.com.kaema.ponto.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Entity que espelha a tabela 'codigos_rfid'.
 *
 * ATENCAO: esta e a tabela onde o ESP32 (hardware em campo) grava cada
 * leitura de tag. E o coracao do tempo real do sistema.
 * NUNCA alteramos a ESTRUTURA dela. O backend apenas:
 *   - LE as leituras pendentes (status = 'pendente')
 *   - ATUALIZA o status para 'processado' apos tratar
 */
@Entity
@Table(name = "codigos_rfid")
@Getter
@Setter
public class RfidCode {

    // int(11) AUTO_INCREMENT — gerado pelo banco a cada leitura do ESP
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    // varchar(255) NOT NULL — o codigo da tag RFID lida
    @Column(name = "codigo", nullable = false, length = 255)
    private String codigo;

    // datetime NOT NULL — quando a leitura aconteceu.
    // 'datetime' do SQL vira LocalDateTime no Java (data + hora).
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    // varchar(50) NOT NULL DEFAULT 'pendente' — estado do processamento
    @Column(name = "status", nullable = false, length = 50)
    private String status;
}