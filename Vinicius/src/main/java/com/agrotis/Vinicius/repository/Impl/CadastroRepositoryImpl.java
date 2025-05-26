package com.agrotis.Vinicius.repository.Impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

import com.agrotis.Vinicius.dto.CadastroFiltroDTO;
import com.agrotis.Vinicius.dto.CadastroLaboratorioResumoDTO;
import com.agrotis.Vinicius.model.Cadastro;
import com.agrotis.Vinicius.model.Laboratorio;
import com.agrotis.Vinicius.repository.custom.CadastroRepositoryCustom;

@Repository
public class CadastroRepositoryImpl implements CadastroRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CadastroLaboratorioResumoDTO> buscarResumoPorLaboratorioComFiltro(CadastroFiltroDTO filtro) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CadastroLaboratorioResumoDTO> cq = cb.createQuery(CadastroLaboratorioResumoDTO.class);

        Root<Cadastro> cadastro = cq.from(Cadastro.class);
        Join<Cadastro, Laboratorio> laboratorio = cadastro.join("laboratorio");

        cq.groupBy(laboratorio.get("id"), laboratorio.get("nome"));

        cq.select(cb.construct(
                CadastroLaboratorioResumoDTO.class,
                laboratorio.get("id"),
                laboratorio.get("nome"),
                cb.count(cadastro)
        ));

        List<Predicate> predicates = new ArrayList<>();

        if (filtro.getDataInicialInicio() != null && filtro.getDataInicialFim() != null) {
            predicates.add(cb.between(cadastro.get("dataInicial"), filtro.getDataInicialInicio(), filtro.getDataInicialFim()));
        }

        if (filtro.getDataFinalInicio() != null && filtro.getDataFinalFim() != null) {
            predicates.add(cb.between(cadastro.get("dataFinal"), filtro.getDataFinalInicio(), filtro.getDataFinalFim()));
        }

        if (filtro.getObservacoes() != null && !filtro.getObservacoes().isEmpty()) {
            predicates.add(cb.like(cb.lower(cadastro.get("observacoes")), "%" + filtro.getObservacoes().toLowerCase() + "%"));
        }

        cq.where(predicates.toArray(new Predicate[0]));
        cq.having(cb.greaterThanOrEqualTo(cb.count(cadastro), filtro.getQuantidadeMinima()));

        List<Order> orderList = new ArrayList<>();
        orderList.add(cb.desc(cb.count(cadastro)));

        if (filtro.getDataInicialInicio() != null || filtro.getDataInicialFim() != null) {
            orderList.add(cb.asc(cb.min(cadastro.get("dataInicial"))));
        }

        cq.orderBy(orderList);

        return entityManager.createQuery(cq).getResultList();
    }
}
