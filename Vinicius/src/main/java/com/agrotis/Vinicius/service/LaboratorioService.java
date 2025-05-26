package com.agrotis.Vinicius.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agrotis.Vinicius.dto.request.LaboratorioRequestDTO;
import com.agrotis.Vinicius.dto.response.LaboratorioResponseDTO;
import com.agrotis.Vinicius.model.Laboratorio;
import com.agrotis.Vinicius.repository.LaboratorioRepository;

@Service
public class LaboratorioService {

    @Autowired
    private LaboratorioRepository repository;

    public LaboratorioResponseDTO salvar(LaboratorioRequestDTO dto) {
        Laboratorio entidade = Laboratorio.builder()
                .nome(dto.getNome())
                .build();
        return toDTO(repository.save(entidade));
    }

    public List<LaboratorioResponseDTO> listarTodos() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public LaboratorioResponseDTO buscarPorId(Long id) {
        Laboratorio entidade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));
        return toDTO(entidade);
    }

    public LaboratorioResponseDTO atualizar(Long id, LaboratorioRequestDTO dto) {
        Laboratorio entidade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));
        entidade.setNome(dto.getNome());
        return toDTO(repository.save(entidade));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Laboratório não encontrado");
        }
        repository.deleteById(id);
    }

    private LaboratorioResponseDTO toDTO(Laboratorio entidade) {
        return LaboratorioResponseDTO.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .build();
    }
}
