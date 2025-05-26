package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.ClienteRequestDTO;
import br.com.agibank.teste.pedidos.dto.ClienteResponseDTO;
import br.com.agibank.teste.pedidos.entities.Cliente;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.exception.RegraNegocioException;
import br.com.agibank.teste.pedidos.mapper.ClienteMapper;
import br.com.agibank.teste.pedidos.repository.ClienteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private ClienteMapper clienteMapper;

    @InjectMocks
    private ClienteServiceImpl clienteService;

    private Cliente cliente;
    private ClienteRequestDTO request;
    private ClienteResponseDTO response;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        cliente = new Cliente();
        cliente.setId(1L);
        cliente.setEmail("cliente@teste.com");
        cliente.setCpfCnpj("12345678901");

        request = new ClienteRequestDTO(1L, "Teste mock", "12345678901", "cliente@teste.com", "12112345678");
        response = new ClienteResponseDTO(1L, "Teste mock", "12345678901", "cliente@teste.com", "12112345678");
    }

    @Test
    void deveCadastrarCliente() {
        when(clienteRepository.existsByCpfCnpj(request.cpfCnpj())).thenReturn(false);
        when(clienteMapper.toEntity(request)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDTOResponse(cliente)).thenReturn(response);

        ClienteResponseDTO result = clienteService.cadastrarCliente(request);

        assertEquals(response, result);
        verify(clienteRepository).save(cliente);
    }

    @Test
    void naoDeveCadastrarClienteDuplicado() {
        when(clienteRepository.existsByCpfCnpj(request.cpfCnpj())).thenReturn(true);
        assertThrows(RegraNegocioException.class, () -> clienteService.cadastrarCliente(request));
    }

    @Test
    void deveAtualizarCliente() {
        when(clienteRepository.existsById(request.id())).thenReturn(true);
        when(clienteMapper.toEntity(request)).thenReturn(cliente);
        when(clienteRepository.save(cliente)).thenReturn(cliente);
        when(clienteMapper.toDTOResponse(cliente)).thenReturn(response);

        ClienteResponseDTO result = clienteService.atualizarCliente(request);
        assertEquals(response, result);
    }

    @Test
    void naoDeveAtualizarClienteInexistente() {
        when(clienteRepository.existsById(request.id())).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class, () -> clienteService.atualizarCliente(request));
    }

    @Test
    void deveBuscarPorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTOResponse(cliente)).thenReturn(response);

        ClienteResponseDTO result = clienteService.findById(1L);
        assertEquals(response, result);
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarPorId() {
        when(clienteRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RecursoNaoEncontradoException.class, () -> clienteService.findById(1L));
    }

    @Test
    void deveBuscarPorEmail() {
        when(clienteRepository.findByEmail("cliente@teste.com")).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTOResponse(cliente)).thenReturn(response);

        ClienteResponseDTO result = clienteService.findByEmail("cliente@teste.com");
        assertEquals(response, result);
    }

    @Test
    void deveBuscarPorCpfCnpj() {
        when(clienteRepository.findByCpfCnpj("12345678901")).thenReturn(Optional.of(cliente));
        when(clienteMapper.toDTOResponse(cliente)).thenReturn(response);

        ClienteResponseDTO result = clienteService.findByCpfCnpj("12345678901");
        assertEquals(response, result);
    }

    @Test
    void deveExcluirCliente() {
        when(clienteRepository.existsById(1L)).thenReturn(true);
        clienteService.excluirCliente(1L);
        verify(clienteRepository).deleteById(1L);
    }

    @Test
    void naoDeveExcluirClienteInexistente() {
        when(clienteRepository.existsById(1L)).thenReturn(false);
        assertThrows(RecursoNaoEncontradoException.class, () -> clienteService.excluirCliente(1L));
    }

    @Test
    void deveListarTodosClientes() {
        Page<Cliente> page = new PageImpl<>(List.of(cliente));
        when(clienteRepository.findAll(any(PageRequest.class))).thenReturn(page);
        when(clienteMapper.toDTOResponse(cliente)).thenReturn(response);

        Page<ClienteResponseDTO> result = clienteService.findAll(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals(response, result.getContent().get(0));
    }
}