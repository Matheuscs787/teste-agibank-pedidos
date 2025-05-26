package br.com.agibank.teste.pedidos.mapper;

import br.com.agibank.teste.pedidos.dto.ProdutoRequestDTO;
import br.com.agibank.teste.pedidos.dto.ProdutoResponseDTO;
import br.com.agibank.teste.pedidos.entities.Produto;
import org.springframework.stereotype.Component;

@Component
public class ProdutoMapper {

    public Produto toEntity(ProdutoRequestDTO dto){
        return new Produto(dto.id(), dto.nome(), dto.descricao(), dto.preco());
    }

    public ProdutoResponseDTO toDTOResponse(Produto entity){
        return new ProdutoResponseDTO(entity.getId(), entity.getNome(), entity.getDescricao(), entity.getPrecoAtual());
    }
}
