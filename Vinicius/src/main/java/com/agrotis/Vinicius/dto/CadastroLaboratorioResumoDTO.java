package com.agrotis.Vinicius.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CadastroLaboratorioResumoDTO {

    private Long laboratorioId;
    private String nomeLaboratorio;
    private Long quantidadePessoas;

    public CadastroLaboratorioResumoDTO(Long laboratorioId, String nomeLaboratorio, Long quantidadePessoas) {
        this.laboratorioId = laboratorioId;
        this.nomeLaboratorio = nomeLaboratorio.toUpperCase();
        this.quantidadePessoas = quantidadePessoas;
    }
}
