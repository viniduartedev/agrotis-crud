package com.agrotis.Vinicius.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.agrotis.Vinicius.dto.CadastroFiltroDTO;
import com.agrotis.Vinicius.dto.CadastroLaboratorioResumoDTO;
import com.agrotis.Vinicius.dto.request.CadastroRequestDTO;
import com.agrotis.Vinicius.dto.response.CadastroResponseDTO;
import com.agrotis.Vinicius.service.CadastroService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/cadastros")
public class CadastroController {

    @Autowired
    private CadastroService service;

    @PostMapping
    public CadastroResponseDTO criar(@RequestBody @Valid CadastroRequestDTO dto) {
        return service.salvar(dto);
    }


    @GetMapping
    public List<CadastroResponseDTO> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public CadastroResponseDTO buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public CadastroResponseDTO atualizar(@PathVariable Long id, @RequestBody CadastroRequestDTO dto) {
        return service.atualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @GetMapping("/resumo-laboratorios")
    public ResponseEntity<List<CadastroLaboratorioResumoDTO>> listarResumoPorLaboratorio(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicialInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicialFim,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinalInicio,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFinalFim,
            @RequestParam(required = false) String observacoes,
            @RequestParam int quantidadeMinima
    ) {
        CadastroFiltroDTO filtro = new CadastroFiltroDTO(
                dataInicialInicio != null ? dataInicialInicio.atStartOfDay() : null,
                dataInicialFim != null ? dataInicialFim.atTime(23, 59, 59) : null,
                dataFinalInicio != null ? dataFinalInicio.atStartOfDay() : null,
                dataFinalFim != null ? dataFinalFim.atTime(23, 59, 59) : null,
                observacoes,
                (long) quantidadeMinima
        );

        List<CadastroLaboratorioResumoDTO> resumo = service.listarResumoPorLaboratorio(filtro);
        return ResponseEntity.ok(resumo);
    }

}
