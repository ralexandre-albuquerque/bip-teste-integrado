# Backend Module - API Gateway & Integration Layer

Este módulo é a porta de entrada da aplicação Java, desenvolvido com **Spring Boot 3.x**. Ele é responsável por expor os serviços de negócio para o mundo externo de forma segura e documentada.

## 🛠 Tecnologias e Padrões

- **Spring Boot 3.x**: Framework base para a API REST.
- **OpenAPI 3.0 (Swagger)**: Documentação automatizada e interativa.
- **JNDI/RMI Client**: Integração transparente com o módulo EJB.
- **CORS Configuration**: Controle de acesso para o frontend Angular.

## 🎯 Destaques de Implementação

- **Clean Controller**: Uso de interfaces no pacote `openapi` para separar metadados de documentação da lógica do Controller.
- **Global Exception Handler**: Centralização do tratamento de erros, convertendo `BusinessException` em respostas HTTP semânticas (400, 404, etc.).
- **DTO Mapping**: Isolamento total entre as entidades de banco de dados e os objetos expostos na API.

## 🚀 Execução e Documentação

Para iniciar apenas este módulo (requer que os módulos `domain` e `ejb` tenham sido instalados via `mvn install`):

```bash
mvn spring-boot:run -pl backend-module
```

- **Swagger UI**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
