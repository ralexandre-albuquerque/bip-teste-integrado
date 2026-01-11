# Backend Module

API REST do sistema de gestão de benefícios.

## Tecnologias

- Spring Boot 3.3.11
- Spring Web MVC
- Spring Data JPA
- H2 Database
- Springdoc OpenAPI 2.8.15

## Configuração

Configurações em `src/main/resources/application.yml`:
- Porta: 8080
- H2 Console: `/h2-console`
- Swagger UI: `/swagger-ui.html`

## Executar

```bash
mvn spring-boot:run
```

## Acessar

- API: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html
- H2 Console: http://localhost:8080/h2-console
