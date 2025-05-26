package br.com.agibank.teste.pedidos.repository;

import br.com.agibank.teste.pedidos.entities.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    Page<Pedido> findByClienteCpfCnpj(String cpfCnpj, Pageable pageable);

}
