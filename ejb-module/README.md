# EJB Module - Core de Negócio e Transacionalidade

Este módulo é o coração da aplicação. Ele foi projetado para gerenciar as regras de negócio mais críticas do sistema, garantindo que as operações financeiras (transferências) sejam atômicas e seguras.

## 🛠 Tecnologias e Padrões

- **EJB 3.2 (Stateless Beans)**: Gerenciamento de ciclo de vida e transações pelo container.
- **Optimistic Locking**: Uso de `@Version` para controle de concorrência sem bloqueios pesados no banco.
- **Rich Domain Model**: Lógica de negócio encapsulada nas entidades, evitando serviços anêmicos.

## 🎯 Destaques de Implementação

- **Transferência Segura**: Implementação de lógica de débito e crédito com validação de saldo e rollback automático em caso de falhas.
- **Comunicação Distribuída**: Exposição de serviços para lookup via **JNDI** e chamadas **RMI**.

## 🧪 Estratégia de Testes

Como este módulo contém a lógica crítica, ele possui cobertura rigorosa:

- **Testes Unitários**: Uso de **JUnit 5** e **Mockito** para validar a orquestração do serviço de forma isolada.
- **Testes de Integração**: Uso de **Testcontainers** com PostgreSQL real para validar o comportamento do `@Version` e garantir que o Rollback transacional funcione perfeitamente.

### Como rodar os testes:

```bash
mvn test -pl ejb-module
```
