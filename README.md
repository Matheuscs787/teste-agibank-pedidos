# teste-agibank-pedidos

Para executar o projeto eu escolhi usar o Kafka e o MySQL, junto com a versão mais recente do Spring e o Java 21.
Para as configurações de ambiente, usei o Docker.

Criei um esquema básico com cadastro de produtos e clientes até chegar no pedido, assim que o pedido é realizado o status do pedido começa a ser atualizado, ao chegar no status AGUARDANDO_PAGAMENTO eu envio para o Kafka.
O serviço de pagamento faz o fluxo e adiciona novamente no Kafka com o novo status do pedido.
Pego no Kafka o pedido com seu novo status, atualizo o banco e vou seguindo o fluxo dependendo do status do pagamento.
