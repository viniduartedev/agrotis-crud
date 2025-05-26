package com.agrotis.Vinicius.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agrotis.Vinicius.dto.request.LaboratorioRequestDTO;
import com.agrotis.Vinicius.dto.response.LaboratorioResponseDTO;
import com.agrotis.Vinicius.service.LaboratorioService;

@RestController
@RequestMapping("/api/laboratorios")
public class LaboratorioController {

    @Autowired
    private LaboratorioService service;

    @PostMapping
    public LaboratorioResponseDTO criar(@RequestBody LaboratorioRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<LaboratorioResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public LaboratorioResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public LaboratorioResponseDTO atualizar(@PathVariable Long id, @RequestBody LaboratorioRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
