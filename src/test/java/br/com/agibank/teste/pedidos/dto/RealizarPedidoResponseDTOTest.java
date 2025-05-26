package br.com.agibank.teste.pedidos.dto;

import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RealizarPedidoResponseDTOTest {

    private Validator validator;

    @BeforeEach
    void setup() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private RealizarPedidoResponseDTO criarDTOValido() {
        ClientePedidoResponseDTO cliente = new ClientePedidoResponseDTO(1L, "Cliente Teste", "12345678901", "email@teste.com", "32123451234");
        ItemPedidoResponseDTO item = new ItemPedidoResponseDTO(1L, "Produto teste", 2, new BigDecimal("10.00"));
        PagamentoDTO pagamento = new PagamentoDTO(FormaPagamento.CARTAO_CREDITO, 1);

        return new RealizarPedidoResponseDTO(
                cliente,
                List.of(item),
                pagamento,
                new BigDecimal("20.00"),
                StatusPedido.CRIADO
        );
    }

    @Test
    void deveSerValidoQuandoTodosOsCamposEstaoPreenchidos() {
        RealizarPedidoResponseDTO dto = criarDTOValido();

        Set<ConstraintViolation<RealizarPedidoResponseDTO>> violations = validator.validate(dto);

        assertTrue(violations.isEmpty(), "Não deveria haver violações de validação");
    }

    @Test
    void deveFalharQuandoClienteForNulo() {
        RealizarPedidoResponseDTO dto = new RealizarPedidoResponseDTO(
                null,
                List.of(new ItemPedidoResponseDTO(1L, "Produto teste", 2, new BigDecimal("10.00"))),
                new PagamentoDTO(FormaPagamento.CARTAO_CREDITO, 1),
                new BigDecimal("20.00"),
                StatusPedido.CRIADO
        );

        Set<ConstraintViolation<RealizarPedidoResponseDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Os dados do cliente são obrigatórios")));
    }

    @Test
    void deveFalharQuandoItensEstiveremVazios() {
        RealizarPedidoResponseDTO dto = new RealizarPedidoResponseDTO(
                new ClientePedidoResponseDTO(1L, "Cliente Teste", "12345678901", "email@teste.com", "32123451234"),
                List.of(),
                new PagamentoDTO(FormaPagamento.CARTAO_CREDITO, 1),
                new BigDecimal("20.00"),
                StatusPedido.CRIADO
        );

        Set<ConstraintViolation<RealizarPedidoResponseDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("O pedido deve conter ao menos um produto")));
    }

    @Test
    void deveFalharQuandoPagamentoForNulo() {
        RealizarPedidoResponseDTO dto = new RealizarPedidoResponseDTO(
                new ClientePedidoResponseDTO(1L, "Cliente Teste", "12345678901", "email@teste.com", "32123451234"),
                List.of(new ItemPedidoResponseDTO(1L, "Produto teste", 2, new BigDecimal("10.00"))),
                null,
                new BigDecimal("20.00"),
                StatusPedido.CRIADO
        );

        Set<ConstraintViolation<RealizarPedidoResponseDTO>> violations = validator.validate(dto);

        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("É necessário informar a forma de pagamento")));
    }
}
