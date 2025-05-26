package com.agrotis.Vinicius.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.agrotis.Vinicius.dto.CadastroFiltroDTO;
import com.agrotis.Vinicius.dto.CadastroLaboratorioResumoDTO;
import com.agrotis.Vinicius.dto.request.CadastroRequestDTO;
import com.agrotis.Vinicius.dto.response.CadastroResponseDTO;
import com.agrotis.Vinicius.dto.response.LaboratorioResponseDTO;
import com.agrotis.Vinicius.dto.response.PropriedadeResponseDTO;
import com.agrotis.Vinicius.exception.DataInvalidaException;
import com.agrotis.Vinicius.model.Cadastro;
import com.agrotis.Vinicius.model.Laboratorio;
import com.agrotis.Vinicius.model.Propriedade;
import com.agrotis.Vinicius.repository.CadastroRepository;
import com.agrotis.Vinicius.repository.LaboratorioRepository;
import com.agrotis.Vinicius.repository.PropriedadeRepository;

@Service
public class CadastroService {

    @Autowired
    private CadastroRepository cadastroRepository;

    @Autowired
    private PropriedadeRepository propriedadeRepository;

    @Autowired
    private LaboratorioRepository laboratorioRepository;

    public CadastroResponseDTO salvar(CadastroRequestDTO dto) {
        if (dto.getDataInicial().isAfter(dto.getDataFinal())) {
            throw new DataInvalidaException("Data inicial não pode ser posterior à data final.");
        }

        Propriedade propriedade = propriedadeRepository.findById(dto.getInfosPropriedadeId())
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));

        Laboratorio laboratorio = laboratorioRepository.findById(dto.getLaboratorioId())
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));

        Cadastro cadastro = Cadastro.builder()
                .nome(dto.getNome())
                .dataInicial(dto.getDataInicial())
                .dataFinal(dto.getDataFinal())
                .observacoes(dto.getObservacoes())
                .infosPropriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        return toDTO(cadastroRepository.save(cadastro));
    }

    public List<CadastroResponseDTO> listarTodos() {
        return cadastroRepository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public CadastroResponseDTO buscarPorId(Long id) {
        Cadastro cadastro = cadastroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));
        return toDTO(cadastro);
    }

    public CadastroResponseDTO atualizar(Long id, CadastroRequestDTO dto) {
        if (dto.getDataInicial().isAfter(dto.getDataFinal())) {
            throw new DataInvalidaException("Data inicial não pode ser posterior à data final.");
        }

        Cadastro cadastro = cadastroRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cadastro não encontrado"));

        Propriedade propriedade = propriedadeRepository.findById(dto.getInfosPropriedadeId())
                .orElseThrow(() -> new RuntimeException("Propriedade não encontrada"));

        Laboratorio laboratorio = laboratorioRepository.findById(dto.getLaboratorioId())
                .orElseThrow(() -> new RuntimeException("Laboratório não encontrado"));

        cadastro.setNome(dto.getNome());
        cadastro.setDataInicial(dto.getDataInicial());
        cadastro.setDataFinal(dto.getDataFinal());
        cadastro.setObservacoes(dto.getObservacoes());
        cadastro.setInfosPropriedade(propriedade);
        cadastro.setLaboratorio(laboratorio);

        return toDTO(cadastroRepository.save(cadastro));
    }

    public void deletar(Long id) {
        if (!cadastroRepository.existsById(id)) {
            throw new RuntimeException("Cadastro não encontrado");
        }
        cadastroRepository.deleteById(id);
    }

    private CadastroResponseDTO toDTO(Cadastro cadastro) {
        return CadastroResponseDTO.builder()
                .id(cadastro.getId())
                .nome(cadastro.getNome())
                .dataInicial(cadastro.getDataInicial())
                .dataFinal(cadastro.getDataFinal())
                .observacoes(cadastro.getObservacoes())
                .infosPropriedade(PropriedadeResponseDTO.builder()
                        .id(cadastro.getInfosPropriedade().getId())
                        .nome(cadastro.getInfosPropriedade().getNome())
                        .build())
                .laboratorio(LaboratorioResponseDTO.builder()
                        .id(cadastro.getLaboratorio().getId())
                        .nome(cadastro.getLaboratorio().getNome())
                        .build())
                .build();
    }

    public List<CadastroLaboratorioResumoDTO> listarResumoPorLaboratorio(CadastroFiltroDTO filtro) {
        return cadastroRepository.buscarResumoPorLaboratorioComFiltro(filtro);
    }
}
