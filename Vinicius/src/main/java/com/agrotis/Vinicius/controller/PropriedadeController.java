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

import com.agrotis.Vinicius.dto.request.PropriedadeRequestDTO;
import com.agrotis.Vinicius.dto.response.PropriedadeResponseDTO;
import com.agrotis.Vinicius.service.PropriedadeService;

@RestController
@RequestMapping("/api/propriedades")
public class PropriedadeController {

    @Autowired
    private PropriedadeService service;

    @PostMapping
    public PropriedadeResponseDTO criar(@RequestBody PropriedadeRequestDTO dto) {
        return service.salvar(dto);
    }

    @GetMapping
    public List<PropriedadeResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public PropriedadeResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public PropriedadeResponseDTO atualizar(@PathVariable Long id, @RequestBody PropriedadeRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }
}
