package com.agrotis.Vinicius.repository.custom;

import java.util.List;

import com.agrotis.Vinicius.dto.CadastroFiltroDTO;
import com.agrotis.Vinicius.dto.CadastroLaboratorioResumoDTO;

public interface CadastroRepositoryCustom {

    List<CadastroLaboratorioResumoDTO> buscarResumoPorLaboratorioComFiltro(CadastroFiltroDTO filtro);
}
