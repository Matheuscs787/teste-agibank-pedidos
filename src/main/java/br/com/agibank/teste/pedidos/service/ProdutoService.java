package br.com.agibank.teste.pedidos.service;

import br.com.agibank.teste.pedidos.dto.ProdutoRequestDTO;
import br.com.agibank.teste.pedidos.dto.ProdutoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProdutoService {

    ProdutoResponseDTO salvar(ProdutoRequestDTO produto);
    ProdutoResponseDTO atualizar(ProdutoRequestDTO produto);
    void deletar(Long idProduto);
    ProdutoResponseDTO findById(Long id);
    Page<ProdutoResponseDTO> findAll(Pageable pageable);
    Page<ProdutoResponseDTO> findByNome(String nome, Pageable pageable);
}
