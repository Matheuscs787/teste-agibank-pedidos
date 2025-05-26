package br.com.agibank.teste.pedidos.service;

import br.com.agibank.teste.pedidos.dto.ClienteRequestDTO;
import br.com.agibank.teste.pedidos.dto.ClienteResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ClienteService {
    ClienteResponseDTO cadastrarCliente(ClienteRequestDTO clienteRequestDTO);
    ClienteResponseDTO atualizarCliente(ClienteRequestDTO clienteRequestDTO);
    ClienteResponseDTO findById(Long id);
    ClienteResponseDTO findByEmail(String email);
    ClienteResponseDTO findByCpfCnpj(String cpfCnpj);
    void excluirCliente(Long id);
    Page<ClienteResponseDTO> findAll(Pageable pageable);
}
