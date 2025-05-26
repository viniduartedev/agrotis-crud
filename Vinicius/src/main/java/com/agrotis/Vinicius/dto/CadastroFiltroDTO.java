package com.agrotis.Vinicius.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CadastroFiltroDTO {

    private LocalDateTime dataInicialInicio;
    private LocalDateTime dataInicialFim;

    private LocalDateTime dataFinalInicio;
    private LocalDateTime dataFinalFim;

    private String observacoes;

    private Long quantidadeMinima;

}
