package br.com.agibank.teste.pedidos.repository;

import br.com.agibank.teste.pedidos.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByCpfCnpj(String cpfCnpj);
    Optional<Cliente> findByEmail(String email);
    boolean existsByCpfCnpj(String cpfCnpj);
}
