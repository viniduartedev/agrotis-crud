package com.agrotis.Vinicius.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agrotis.Vinicius.dto.request.PropriedadeRequestDTO;
import com.agrotis.Vinicius.dto.response.PropriedadeResponseDTO;
import com.agrotis.Vinicius.model.Propriedade;
import com.agrotis.Vinicius.repository.PropriedadeRepository;

@Service
public class PropriedadeService {

    @Autowired
    private PropriedadeRepository repository;

    public PropriedadeResponseDTO salvar(PropriedadeRequestDTO dto) {
        Propriedade entidade = Propriedade.builder()
                .nome(dto.getNome())
                .build();
        entidade = repository.save(entidade);
        return toDTO(entidade);
    }

    public List<PropriedadeResponseDTO> listarTodos() {
        return repository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public PropriedadeResponseDTO buscarPorId(Long id) {
        Propriedade entidade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));
        return toDTO(entidade);
    }

    public PropriedadeResponseDTO atualizar(Long id, PropriedadeRequestDTO dto) {
        Propriedade entidade = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));
        entidade.setNome(dto.getNome());
        return toDTO(repository.save(entidade));
    }

    public void deletar(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Propriedade não encontrada");
        }
        repository.deleteById(id);
    }

    private PropriedadeResponseDTO toDTO(Propriedade entidade) {
        return PropriedadeResponseDTO.builder()
                .id(entidade.getId())
                .nome(entidade.getNome())
                .build();
    }
}
