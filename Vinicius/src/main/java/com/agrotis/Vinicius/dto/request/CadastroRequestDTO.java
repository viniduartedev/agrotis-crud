package com.agrotis.Vinicius.dto.request;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadastroRequestDTO {

    @NotBlank(message = "O nome é obrigatório.")
    private String nome;

    @NotNull(message = "A data inicial é obrigatória.")
    private LocalDateTime dataInicial;

    @NotNull(message = "A data final é obrigatória.")
    private LocalDateTime dataFinal;

    @NotNull(message = "O ID da propriedade é obrigatório.")
    private Long infosPropriedadeId;

    @NotNull(message = "O ID do laboratório é obrigatório.")
    private Long laboratorioId;

    @Size(max = 100, message = "O nome deve ter no max 100 caracteres.")
    private String observacoes;
}
