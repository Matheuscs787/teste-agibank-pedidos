package br.com.agibank.teste.pedidos.producer;


import br.com.agibank.teste.pedidos.dto.ProcessarPagamentoDTO;
import br.com.agibank.teste.pedidos.enums.FormaPagamento;
import br.com.agibank.teste.pedidos.enums.StatusPedido;
import br.com.agibank.teste.pedidos.util.TestUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PagamentoRequestProducerTest {

    @InjectMocks
    private PagamentoRequestProducer producer;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @Captor
    private ArgumentCaptor<String> captor;

    private final String topico = "topico-pagamento-request";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        producer = new PagamentoRequestProducer(objectMapper, kafkaTemplate);

        TestUtils.setField(producer, "topicoPagamentoRequest", topico);
    }

    @Test
    void deveEnviarMensagemParaOTopicoKafkaComSucesso() throws Exception {
        ProcessarPagamentoDTO dto = new ProcessarPagamentoDTO(1L, FormaPagamento.CARTAO_CREDITO, 1, new BigDecimal("100.0"), StatusPedido.PAGAMENTO_APROVADO);
        String jsonEsperado = "{\"idPedido\":1,\"statusPedido\":\"PAGAMENTO_APROVADO\"}";

        when(objectMapper.writeValueAsString(dto)).thenReturn(jsonEsperado);

        String retorno = producer.enviarParaPagamento(dto);

        verify(kafkaTemplate).send(eq(topico), captor.capture());
        assertEquals(jsonEsperado, captor.getValue());
        assertEquals("Pagamento enviado para processamento", retorno);
    }

    @Test
    void deveLancarExcecaoQuandoSerializacaoFalhar() throws Exception {
        ProcessarPagamentoDTO dto = new ProcessarPagamentoDTO(1L, FormaPagamento.CARTAO_CREDITO, 1, new BigDecimal("100.0"), StatusPedido.PAGAMENTO_APROVADO);
        when(objectMapper.writeValueAsString(dto)).thenThrow(new JsonProcessingException("Erro") {});

        try {
            producer.enviarParaPagamento(dto);
        } catch (JsonProcessingException e) {
            assertEquals("Erro", e.getOriginalMessage());
            verify(kafkaTemplate, never()).send(any(), any());
        }
    }
}