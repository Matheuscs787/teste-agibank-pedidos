package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.ProdutoRequestDTO;
import br.com.agibank.teste.pedidos.dto.ProdutoResponseDTO;
import br.com.agibank.teste.pedidos.entities.Produto;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.mapper.ProdutoMapper;
import br.com.agibank.teste.pedidos.repository.ProdutoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceImplTest {

    @InjectMocks
    private ProdutoServiceImpl produtoService;

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private ProdutoMapper produtoMapper;

    private Produto produto;
    private ProdutoRequestDTO produtoRequestDTO;
    private ProdutoResponseDTO produtoResponseDTO;

    @BeforeEach
    void setUp() {
        produto = new Produto(1L, "Notebook", "Notebook de testes", new BigDecimal("3500.0"));
        produtoRequestDTO = new ProdutoRequestDTO(1l, "Notebook", "Notebook de testes", new BigDecimal("3500.0"));
        produtoResponseDTO = new ProdutoResponseDTO(1l, "Notebook", "Notebook de testes", new BigDecimal("3500.0"));
    }

    @Test
    void deveSalvarProdutoComSucesso() {
        when(produtoMapper.toEntity(produtoRequestDTO)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toDTOResponse(produto)).thenReturn(produtoResponseDTO);

        ProdutoResponseDTO result = produtoService.salvar(produtoRequestDTO);

        assertEquals(produtoResponseDTO, result);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveAtualizarProdutoComSucesso() {
        when(produtoRepository.existsById(produtoRequestDTO.id())).thenReturn(true);
        when(produtoMapper.toEntity(produtoRequestDTO)).thenReturn(produto);
        when(produtoRepository.save(produto)).thenReturn(produto);
        when(produtoMapper.toDTOResponse(produto)).thenReturn(produtoResponseDTO);

        ProdutoResponseDTO result = produtoService.atualizar(produtoRequestDTO);

        assertEquals(produtoResponseDTO, result);
        verify(produtoRepository).save(produto);
    }

    @Test
    void deveLancarExcecaoAoAtualizarProdutoInexistente() {
        when(produtoRepository.existsById(produtoRequestDTO.id())).thenReturn(false);

        var ex = assertThrows(RecursoNaoEncontradoException.class,
                () -> produtoService.atualizar(produtoRequestDTO));

        assertTrue(ex.getMessage().contains("Produto com id 1 não encontrado"));
    }

    @Test
    void deveDeletarProdutoComSucesso() {
        when(produtoRepository.existsById(1L)).thenReturn(true);

        produtoService.deletar(1L);

        verify(produtoRepository).deleteById(1L);
    }

    @Test
    void deveLancarExcecaoAoDeletarProdutoInexistente() {
        when(produtoRepository.existsById(1L)).thenReturn(false);

        var ex = assertThrows(RecursoNaoEncontradoException.class,
                () -> produtoService.deletar(1L));

        assertTrue(ex.getMessage().contains("Produto com id 1 não encontrado"));
    }

    @Test
    void deveBuscarProdutoPorIdComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));
        when(produtoMapper.toDTOResponse(produto)).thenReturn(produtoResponseDTO);

        ProdutoResponseDTO result = produtoService.findById(1L);

        assertEquals(produtoResponseDTO, result);
    }

    @Test
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.empty());

        var ex = assertThrows(RecursoNaoEncontradoException.class,
                () -> produtoService.findById(1L));

        assertTrue(ex.getMessage().contains("Produto com id 1 não encontrado"));
    }

    @Test
    void deveBuscarTodosOsProdutosComSucesso() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Produto> page = new PageImpl<>(Collections.singletonList(produto));

        when(produtoRepository.findAll(pageable)).thenReturn(page);
        when(produtoMapper.toDTOResponse(produto)).thenReturn(produtoResponseDTO);

        Page<ProdutoResponseDTO> result = produtoService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(produtoResponseDTO, result.getContent().get(0));
    }

    @Test
    void deveBuscarProdutoPorNomeComSucesso() {
        PageRequest pageable = PageRequest.of(0, 10);
        Page<Produto> page = new PageImpl<>(Collections.singletonList(produto));

        when(produtoRepository.findByNomeContainingIgnoreCase("notebook", pageable)).thenReturn(page);
        when(produtoMapper.toDTOResponse(produto)).thenReturn(produtoResponseDTO);

        Page<ProdutoResponseDTO> result = produtoService.findByNome("notebook", pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(produtoResponseDTO, result.getContent().get(0));
    }
}