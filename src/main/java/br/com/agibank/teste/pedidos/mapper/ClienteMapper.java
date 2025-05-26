package br.com.agibank.teste.pedidos.mapper;

import br.com.agibank.teste.pedidos.dto.ClienteRequestDTO;
import br.com.agibank.teste.pedidos.dto.ClienteResponseDTO;
import br.com.agibank.teste.pedidos.entities.Cliente;
import org.springframework.stereotype.Component;

@Component
public class ClienteMapper {

    public Cliente toEntity(ClienteRequestDTO dto) {
        return new Cliente(dto.id(),
                dto.nome(),
                dto.cpfCnpj(),
                dto.email(),
                dto.telefone()
                );
    }

    public ClienteResponseDTO toDTOResponse(Cliente entity) {
        return new ClienteResponseDTO(entity.getId(),
                entity.getNome(),
                entity.getCpfCnpj(),
                entity.getEmail(),
                entity.getTelefone());
    }

}
