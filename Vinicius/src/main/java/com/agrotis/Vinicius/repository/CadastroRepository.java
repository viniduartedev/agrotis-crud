package com.agrotis.Vinicius.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.agrotis.Vinicius.dto.CadastroLaboratorioResumoDTO;
import com.agrotis.Vinicius.model.Cadastro;
import com.agrotis.Vinicius.repository.custom.CadastroRepositoryCustom;

public interface CadastroRepository extends JpaRepository<Cadastro, Long>, CadastroRepositoryCustom {

    @Query("SELECT new com.agrotis.Vinicius.dto.CadastroLaboratorioResumoDTO("
            + "l.id, l.nome, COUNT(c)) "
            + "FROM Cadastro c "
            + "JOIN c.laboratorio l "
            + "GROUP BY l.id, l.nome")
    List<CadastroLaboratorioResumoDTO> listarResumoPorLaboratorio();

}
