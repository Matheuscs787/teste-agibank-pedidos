package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.ProdutoRequestDTO;
import br.com.agibank.teste.pedidos.dto.ProdutoResponseDTO;
import br.com.agibank.teste.pedidos.entities.Produto;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.mapper.ProdutoMapper;
import br.com.agibank.teste.pedidos.repository.ProdutoRepository;
import br.com.agibank.teste.pedidos.service.ProdutoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoServiceImpl implements ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final ProdutoMapper produtoMapper;

    public ProdutoServiceImpl(ProdutoRepository produtoRepository, ProdutoMapper produtoMapper) {
        this.produtoRepository = produtoRepository;
        this.produtoMapper = produtoMapper;
    }

    @Override
    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO produto) {
        Produto p = produtoRepository.save(produtoMapper.toEntity(produto));
        return produtoMapper.toDTOResponse(p);
    }

    @Override
    @Transactional
    public ProdutoResponseDTO atualizar(ProdutoRequestDTO produto) {
        if(!produtoRepository.existsById(produto.id())) {
            throw new RecursoNaoEncontradoException("Produto com id " + produto.id() + " não encontrado");
        }
        Produto p = produtoRepository.save(produtoMapper.toEntity(produto));
        return produtoMapper.toDTOResponse(p);
    }

    @Override
    @Transactional
    public void deletar(Long idProduto) {
        if(!produtoRepository.existsById(idProduto)) {
            throw new RecursoNaoEncontradoException("Produto com id " + idProduto + " não encontrado");
        }
        produtoRepository.deleteById(idProduto);
    }

    @Override
    public ProdutoResponseDTO findById(Long idProduto) {
        Produto produto = produtoRepository.findById(idProduto).orElseThrow(() -> new RecursoNaoEncontradoException("Produto com id " + idProduto + " não encontrado"));
        return produtoMapper.toDTOResponse(produto);
    }

    @Override
    public Page<ProdutoResponseDTO> findAll(Pageable pageable) {
        return produtoRepository.findAll(pageable).map(produtoMapper::toDTOResponse);
    }

    @Override
    public Page<ProdutoResponseDTO> findByNome(String nome, Pageable pageable) {
        return produtoRepository.findByNomeContainingIgnoreCase(nome, pageable).map(produtoMapper::toDTOResponse);
    }
}
