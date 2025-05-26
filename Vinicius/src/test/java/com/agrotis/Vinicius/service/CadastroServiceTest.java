package com.agrotis.Vinicius.service;

import com.agrotis.Vinicius.model.Cadastro;
import com.agrotis.Vinicius.model.Laboratorio;
import com.agrotis.Vinicius.model.Propriedade;
import com.agrotis.Vinicius.repository.CadastroRepository;
import com.agrotis.Vinicius.repository.LaboratorioRepository;
import com.agrotis.Vinicius.repository.PropriedadeRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.agrotis.Vinicius.dto.CadastroFiltroDTO;
import com.agrotis.Vinicius.dto.CadastroLaboratorioResumoDTO;
import com.agrotis.Vinicius.dto.request.CadastroRequestDTO;
import com.agrotis.Vinicius.dto.response.CadastroResponseDTO;
import com.agrotis.Vinicius.dto.response.LaboratorioResponseDTO;
import com.agrotis.Vinicius.dto.response.PropriedadeResponseDTO;
import com.agrotis.Vinicius.exception.DataInvalidaException;

@ExtendWith(MockitoExtension.class)
class CadastroServiceTest {

    @InjectMocks
    private CadastroService cadastroService;

    @Mock
    private CadastroRepository cadastroRepository;

    @Mock
    private LaboratorioRepository laboratorioRepository;

    @Mock
    private PropriedadeRepository propriedadeRepository;

    private CadastroRequestDTO requestDTO;
    private Laboratorio laboratorio;  
    private Propriedade propriedade;
    
    private PropriedadeResponseDTO propriedadeDTO;
    private LaboratorioResponseDTO laboratorioDTO;  

    @BeforeEach
    void setup() {
        requestDTO = new CadastroRequestDTO();
        requestDTO.setNome("Teste de Cadastro");
        requestDTO.setDataInicial(LocalDateTime.of(2025, 5, 24, 8, 0));
        requestDTO.setDataFinal(LocalDateTime.of(2025, 5, 24, 18, 0));
        requestDTO.setObservacoes("Tudo certo");
        requestDTO.setInfosPropriedadeId(2L);
        requestDTO.setLaboratorioId(3L);

        laboratorio = new Laboratorio();
        laboratorio.setId(3L);
        laboratorio.setNome("Laboratório Modelo 2");

        propriedade = new Propriedade();
        propriedade.setId(2L);
        propriedade.setNome("Fazenda Modelo 1");
        
        propriedadeDTO = new PropriedadeResponseDTO();
        propriedadeDTO.setId(2L);
        propriedadeDTO.setNome("Fazenda Modelo 1");
        
        laboratorioDTO = new LaboratorioResponseDTO();
        laboratorioDTO.setId(3L);
        laboratorioDTO.setNome("Laboratório Modelo 2");
    }

    @Test
    void deveSalvarCadastroComSucesso() {
        // Arrange
        when(propriedadeRepository.findById(2L)).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(3L)).thenReturn(Optional.of(laboratorio));
        when(cadastroRepository.save(any(Cadastro.class))).thenAnswer(i -> i.getArgument(0));

        // Act
        CadastroResponseDTO cadastroSalvo = cadastroService.salvar(requestDTO);

        // Assert
        assertNotNull(cadastroSalvo);
        assertEquals("Teste de Cadastro", cadastroSalvo.getNome());
        assertEquals(propriedadeDTO, cadastroSalvo.getInfosPropriedade());
        assertEquals(laboratorioDTO, cadastroSalvo.getLaboratorio());

        verify(cadastroRepository, times(1)).save(any(Cadastro.class));
    }
    
    @Test
    void deveLancarExcecaoSeDataInicialMaiorQueFinalAoSalvar() {
        requestDTO.setDataInicial(LocalDateTime.of(2025, 5, 25, 18, 0));
        requestDTO.setDataFinal(LocalDateTime.of(2025, 5, 24, 8, 0));

        DataInvalidaException exception = assertThrows(DataInvalidaException.class, () ->
            cadastroService.salvar(requestDTO)
        );

        assertEquals("Data inicial não pode ser posterior à data final.", exception.getMessage());
    }


    @Test
    void deveLancarExcecaoSePropriedadeNaoExistir() {
        // Arrange
        when(propriedadeRepository.findById(100000L)).thenReturn(Optional.empty());
        requestDTO.setInfosPropriedadeId(100000L);
        
        // Act & Assert
        RuntimeException  exception = assertThrows(RuntimeException.class, ()
                -> cadastroService.salvar(requestDTO)
        );
        assertTrue(exception.getMessage().contains("Propriedade não encontrada"));
    }

    @Test
    void deveLancarExcecaoSeLaboratorioNaoExistir() {
        // Arrange
        when(propriedadeRepository.findById(2L)).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(3000L)).thenReturn(Optional.empty());
        
        requestDTO.setLaboratorioId(3000L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, ()
                -> cadastroService.salvar(requestDTO)
        );

        assertTrue(exception.getMessage().contains("Laboratório não encontrado"));
    }
    
    @Test
    void deveListarTodosOsCadastros() {
        // Arrange
        Cadastro cadastro1 = Cadastro.builder()
                .id(1L)
                .nome("Cadastro 1")
                .dataInicial(LocalDateTime.of(2025, 5, 20, 8, 0))
                .dataFinal(LocalDateTime.of(2025, 5, 20, 17, 0))
                .observacoes("Observação 1")
                .infosPropriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        Cadastro cadastro2 = Cadastro.builder()
                .id(2L)
                .nome("Cadastro 2")
                .dataInicial(LocalDateTime.of(2025, 5, 21, 8, 0))
                .dataFinal(LocalDateTime.of(2025, 5, 21, 17, 0))
                .observacoes("Observação 2")
                .infosPropriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        List<Cadastro> cadastros = Arrays.asList(cadastro1, cadastro2);
        when(cadastroRepository.findAll()).thenReturn(cadastros);

        // Act
        List<CadastroResponseDTO> result = cadastroService.listarTodos();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Cadastro 1", result.get(0).getNome());
        assertEquals("Cadastro 2", result.get(1).getNome());

        verify(cadastroRepository, times(1)).findAll();
    }

    @Test
    void deveRetornarCadastroPorId() {
        // Arrange
        Cadastro cadastro = Cadastro.builder()
                .id(1L)
                .nome("Cadastro Teste")
                .dataInicial(LocalDateTime.of(2025, 5, 24, 8, 0))
                .dataFinal(LocalDateTime.of(2025, 5, 24, 17, 0))
                .observacoes("Teste de busca")
                .infosPropriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        when(cadastroRepository.findById(1L)).thenReturn(Optional.of(cadastro));

        // Act
        CadastroResponseDTO response = cadastroService.buscarPorId(1L);

        // Assert
        assertNotNull(response);
        assertEquals("Cadastro Teste", response.getNome());
        assertEquals(2L, response.getInfosPropriedade().getId());
        assertEquals(3L, response.getLaboratorio().getId());

        verify(cadastroRepository, times(1)).findById(1L);
    }
    
    @Test
    void deveLancarExcecaoSeCadastroNaoForEncontrado() {
        // Arrange
        when(cadastroRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cadastroService.buscarPorId(999L)
        );

        assertTrue(exception.getMessage().contains("Cadastro não encontrado"));
        verify(cadastroRepository, times(1)).findById(999L);
    }
    
    @Test
    void deveAtualizarCadastroComSucesso() {
        // Arrange
        Cadastro cadastroExistente = Cadastro.builder()
                .id(1L)
                .nome("Cadastro Antigo")
                .dataInicial(LocalDateTime.of(2025, 5, 20, 8, 0))
                .dataFinal(LocalDateTime.of(2025, 5, 20, 17, 0))
                .observacoes("Observações antigas")
                .infosPropriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        when(cadastroRepository.findById(1L)).thenReturn(Optional.of(cadastroExistente));
        when(propriedadeRepository.findById(2L)).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(3L)).thenReturn(Optional.of(laboratorio));
        when(cadastroRepository.save(any(Cadastro.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        CadastroResponseDTO atualizado = cadastroService.atualizar(1L, requestDTO);

        // Assert
        assertNotNull(atualizado);
        assertEquals("Teste de Cadastro", atualizado.getNome());
        assertEquals("Tudo certo", atualizado.getObservacoes());
        assertEquals(requestDTO.getDataInicial(), atualizado.getDataInicial());
        assertEquals(requestDTO.getDataFinal(), atualizado.getDataFinal());

        verify(cadastroRepository, times(1)).findById(1L);
        verify(cadastroRepository, times(1)).save(any(Cadastro.class));
    }

    @Test
    void deveLancarExcecaoSeCadastroNaoForEncontradoNaAtualizacao() {
        // Arrange
        when(cadastroRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cadastroService.atualizar(999L, requestDTO)
        );

        assertTrue(exception.getMessage().contains("Cadastro não encontrado"));
    }

    @Test
    void deveLancarExcecaoSeDataInicialMaiorQueFinalNaAtualizacao() {
        // Arrange
        requestDTO.setDataInicial(LocalDateTime.of(2025, 5, 25, 18, 0));
        requestDTO.setDataFinal(LocalDateTime.of(2025, 5, 24, 8, 0));

        // Act & Assert
        DataInvalidaException exception = assertThrows(DataInvalidaException.class, () ->
                cadastroService.atualizar(1L, requestDTO)
        );

        assertEquals("Data inicial não pode ser posterior à data final.", exception.getMessage());
    }

    @Test
    void deveLancarExcecaoSePropriedadeNaoForEncontradaNaAtualizacao() {
        Cadastro cadastroExistente = Cadastro.builder()
                .id(1L)
                .infosPropriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        when(cadastroRepository.findById(1L)).thenReturn(Optional.of(cadastroExistente));
        when(propriedadeRepository.findById(2L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cadastroService.atualizar(1L, requestDTO)
        );

        assertTrue(exception.getMessage().contains("Propriedade não encontrada"));
    }

    @Test
    void deveLancarExcecaoSeLaboratorioNaoForEncontradoNaAtualizacao() {
        Cadastro cadastroExistente = Cadastro.builder()
                .id(1L)
                .infosPropriedade(propriedade)
                .laboratorio(laboratorio)
                .build();

        when(cadastroRepository.findById(1L)).thenReturn(Optional.of(cadastroExistente));
        when(propriedadeRepository.findById(2L)).thenReturn(Optional.of(propriedade));
        when(laboratorioRepository.findById(3L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () ->
                cadastroService.atualizar(1L, requestDTO)
        );

        assertTrue(exception.getMessage().contains("Laboratório não encontrado"));
    }

    @Test
    void deveDeletarCadastroComSucesso() {
        // Arrange
        Long id = 1L;
        when(cadastroRepository.existsById(id)).thenReturn(true);
        doNothing().when(cadastroRepository).deleteById(id);

        // Act
        cadastroService.deletar(id);

        // Assert
        verify(cadastroRepository, times(1)).existsById(id);
        verify(cadastroRepository, times(1)).deleteById(id);
    }

    @Test
    void deveLancarExcecaoAoTentarDeletarCadastroInexistente() {
        // Arrange
        Long id = 999L;
        when(cadastroRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> 
            cadastroService.deletar(id)
        );

        assertEquals("Cadastro não encontrado", exception.getMessage());
        verify(cadastroRepository, times(1)).existsById(id);
        verify(cadastroRepository, never()).deleteById(anyLong());
    }
    
    
    @Test
    void deveRetornarListaResumoPorLaboratorio() {
        // Arrange
        CadastroFiltroDTO filtro = new CadastroFiltroDTO(
            LocalDateTime.of(2025, 5, 1, 0, 0),
            LocalDateTime.of(2025, 5, 31, 23, 59),
            null,
            null,
            "sucesso",
            1L
        );

        List<CadastroLaboratorioResumoDTO> resumoEsperado = List.of(
            new CadastroLaboratorioResumoDTO(1L, "Laboratório A", 5L),
            new CadastroLaboratorioResumoDTO(2L, "Laboratório B", 3L)
        );

        when(cadastroRepository.buscarResumoPorLaboratorioComFiltro(filtro)).thenReturn(resumoEsperado);

        // Act
        List<CadastroLaboratorioResumoDTO> resultado = cadastroService.listarResumoPorLaboratorio(filtro);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("LABORATÓRIO A", resultado.get(0).getNomeLaboratorio());
        assertEquals(5L, resultado.get(0).getQuantidadePessoas());

        verify(cadastroRepository, times(1)).buscarResumoPorLaboratorioComFiltro(filtro);
    }

    @Test
    void deveRetornarListaVaziaNoResumoPorLaboratorio() {
        CadastroFiltroDTO filtro = new CadastroFiltroDTO(
            LocalDateTime.of(2025, 5, 1, 0, 0),
            LocalDateTime.of(2025, 5, 31, 23, 59),
            null, null, "sucesso", 1L
        );

        when(cadastroRepository.buscarResumoPorLaboratorioComFiltro(filtro)).thenReturn(Collections.emptyList());

        List<CadastroLaboratorioResumoDTO> resultado = cadastroService.listarResumoPorLaboratorio(filtro);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());

        verify(cadastroRepository, times(1)).buscarResumoPorLaboratorioComFiltro(filtro);
    }


}
