package br.com.agibank.teste.pedidos.service.impl;

import br.com.agibank.teste.pedidos.dto.ClienteRequestDTO;
import br.com.agibank.teste.pedidos.dto.ClienteResponseDTO;
import br.com.agibank.teste.pedidos.entities.Cliente;
import br.com.agibank.teste.pedidos.exception.RecursoNaoEncontradoException;
import br.com.agibank.teste.pedidos.exception.RegraNegocioException;
import br.com.agibank.teste.pedidos.mapper.ClienteMapper;
import br.com.agibank.teste.pedidos.repository.ClienteRepository;
import br.com.agibank.teste.pedidos.service.ClienteService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteServiceImpl implements ClienteService {

    public static final String NAO_ENCONTRADO = " não encontrado";
    private final ClienteRepository clienteRepository;
    private final ClienteMapper clienteMapper;


    public ClienteServiceImpl(ClienteRepository clienteRepository, ClienteMapper clienteMapper) {
        this.clienteRepository = clienteRepository;
        this.clienteMapper = clienteMapper;
    }

    @Override
    @Transactional
    public ClienteResponseDTO cadastrarCliente(ClienteRequestDTO clienteRequestDTO) {
        if(clienteRepository.existsByCpfCnpj(clienteRequestDTO.cpfCnpj())){
            throw new RegraNegocioException("Já existe um cliente com este CPF/CNPJ");
        }

        Cliente cliente = clienteRepository.save(clienteMapper.toEntity(clienteRequestDTO));
        return clienteMapper.toDTOResponse(cliente);
    }

    @Override
    @Transactional
    public ClienteResponseDTO atualizarCliente(ClienteRequestDTO clienteRequestDTO) {
        if(!clienteRepository.existsById(clienteRequestDTO.id())){
            throw new RecursoNaoEncontradoException("Cliente com id " + clienteRequestDTO.id() + NAO_ENCONTRADO);
        }
        Cliente cliente = clienteRepository.save(clienteMapper.toEntity(clienteRequestDTO));
        return clienteMapper.toDTOResponse(cliente);
    }

    @Override
    public ClienteResponseDTO findById(Long id) {
        return clienteRepository.findById(id)
                .map(clienteMapper::toDTOResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com id " + id + NAO_ENCONTRADO));
    }

    @Override
    public ClienteResponseDTO findByEmail(String email) {
        return clienteRepository.findByEmail(email)
                .map(clienteMapper::toDTOResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com e-mail: " + email + NAO_ENCONTRADO));
    }

    @Override
    public ClienteResponseDTO findByCpfCnpj(String cpfCnpj) {
        return clienteRepository.findByCpfCnpj(cpfCnpj)
                .map(clienteMapper::toDTOResponse)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Cliente com cpfCnpj " + cpfCnpj + NAO_ENCONTRADO));
    }

    @Override
    @Transactional
    public void excluirCliente(Long id) {
        if(!clienteRepository.existsById(id)){
            throw new RecursoNaoEncontradoException("Cliente com id " + id + NAO_ENCONTRADO);
        }
        clienteRepository.deleteById(id);
    }

    @Override
    public Page<ClienteResponseDTO> findAll(Pageable pageable) {
        return clienteRepository.findAll(pageable).map(clienteMapper::toDTOResponse);
    }
}
