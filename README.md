# teste-agibank-pedidos

Para executar o projeto, escolhi usar o Kafka e o MySQL, junto com a versão mais recente do Spring e o Java 21. Já para as configurações de ambiente, usei o Docker.

Criei um esquema básico com cadastro de produtos e clientes até chegar no pedido. Assim que o pedido é realizado, o status do começa a ser atualizado. Quando o status chega em AGUARDANDO_PAGAMENTO, envio para o Kafka.

O serviço de pagamento faz o fluxo e adiciona novamente ao Kafka com o novo status do pedido. Assim, pego o pedido com seu novo status no Kafka, atualizo o banco e vou seguindo o fluxo dependendo do status do pagamento.

Como evolução, seria interessante adicionar uma autenticação do usuário e liberar endpoint de acordo com o nível do usuário, além de usar a autenticação para atrelar o usuário ao pedido e controlar o acesso dos pedidos somente do usuário logado.

Também é interessante lidar com logs de auditoria conforme o pedido segue o fluxo.

Outro ponto que pode ser incrementado são mais detalhes nas entities, pois as resumi com um pequeno conjunto de informações para seguir com o teste. Também podemos agrupar melhor as informações de pagamento conforme coletar mais informações.

Esses são alguns dos pontos que podem ser melhorados em uma versão futura.
