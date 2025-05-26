package com.agrotis.Vinicius.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.agrotis.Vinicius.model.Propriedade;

public interface PropriedadeRepository extends JpaRepository<Propriedade, Long> {
}