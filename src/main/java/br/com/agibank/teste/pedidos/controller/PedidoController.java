package br.com.agibank.teste.pedidos.controller;

import br.com.agibank.teste.pedidos.dto.BuscarPedidoCpfCpnjRequestDTO;
import br.com.agibank.teste.pedidos.dto.BuscarPedidoResponseDTO;
import br.com.agibank.teste.pedidos.dto.RealizarPedidoRequestDTO;
import br.com.agibank.teste.pedidos.dto.RealizarPedidoResponseDTO;
import br.com.agibank.teste.pedidos.service.PedidoService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    public ResponseEntity<RealizarPedidoResponseDTO> realizarPedido(@RequestBody @Valid RealizarPedidoRequestDTO realizarPedidoRequest){
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.save(realizarPedidoRequest));
    }

    @GetMapping("/{idPedido}")
    public ResponseEntity<BuscarPedidoResponseDTO> findById(@PathVariable(value = "idPedido") Long idPedido){
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findById(idPedido));
    }

    @GetMapping()
    public ResponseEntity<Page<BuscarPedidoResponseDTO>> findAll(Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findAll(pageable));
    }

    @PostMapping("/buscar/cpfCnpj")
    public ResponseEntity<Page<BuscarPedidoResponseDTO>> findByClienteCpfCnpj(@RequestBody @Valid BuscarPedidoCpfCpnjRequestDTO buscarPedidoCpfCpnjRequestDTO, Pageable pageable){
        return ResponseEntity.status(HttpStatus.OK).body(pedidoService.findByClienteCpfCnpj(buscarPedidoCpfCpnjRequestDTO.cpfCnpj(), pageable));
    }
}
