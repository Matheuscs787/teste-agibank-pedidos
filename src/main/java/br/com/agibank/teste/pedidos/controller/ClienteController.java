package br.com.agibank.teste.pedidos.controller;

import br.com.agibank.teste.pedidos.dto.ClienteCpfCpnjRequestDTO;
import br.com.agibank.teste.pedidos.dto.ClienteEmailRequestDTO;
import br.com.agibank.teste.pedidos.dto.ClienteRequestDTO;
import br.com.agibank.teste.pedidos.dto.ClienteResponseDTO;
import br.com.agibank.teste.pedidos.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService clienteService;

    public ClienteController(ClienteService clienteService) {
        this.clienteService = clienteService;
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> findAll(@PageableDefault(size = 10) Pageable pageable) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findAll(pageable));
    }

    @GetMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long idCliente) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findById(idCliente));
    }

    @PostMapping("/buscar/email")
    public ResponseEntity<ClienteResponseDTO> findByEmail(@RequestBody @Valid ClienteEmailRequestDTO clienteEmailRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findByEmail(clienteEmailRequestDTO.email()));
    }

    @PostMapping("/buscar/cpfCnpj")
    public ResponseEntity<ClienteResponseDTO> findByCpfCnpj(@RequestBody @Valid ClienteCpfCpnjRequestDTO clienteCpfCpnjRequestDTO){
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.findByCpfCnpj(clienteCpfCpnjRequestDTO.cpfCnpj()));
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> save(@RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.cadastrarCliente(clienteRequestDTO));
    }

    @PutMapping
    public ResponseEntity<ClienteResponseDTO> update(@RequestBody @Valid ClienteRequestDTO clienteRequestDTO) {
        return ResponseEntity.status(HttpStatus.OK).body(clienteService.atualizarCliente(clienteRequestDTO));
    }

    @DeleteMapping("/{idCliente}")
    public ResponseEntity<ClienteResponseDTO> delete(@PathVariable Long idCliente) {
        clienteService.excluirCliente(idCliente);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
