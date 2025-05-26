package br.com.agibank.teste.pedidos.service;

import br.com.agibank.teste.pedidos.dto.BuscarPedidoResponseDTO;
import br.com.agibank.teste.pedidos.dto.RealizarPedidoRequestDTO;
import br.com.agibank.teste.pedidos.dto.RealizarPedidoResponseDTO;
import br.com.agibank.teste.pedidos.entities.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PedidoService {

    Page<BuscarPedidoResponseDTO> findByClienteCpfCnpj(String cpfCnpj, Pageable pageable);
    Page<BuscarPedidoResponseDTO> findAll(Pageable pageable);
    BuscarPedidoResponseDTO findById(Long id);
    RealizarPedidoResponseDTO save(RealizarPedidoRequestDTO realizarPedidoRequestDTO);
    void atualizarStatus(Pedido pedido);
}
