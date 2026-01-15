# BIP - Sistema de Gestão de Benefícios Integrado

Este projeto é uma aplicação Fullstack moderna que demonstra a integração entre o ecossistema **Jakarta EE (EJB)** e **Spring Boot 3.x**, com um frontend robusto em **Angular 17**. O foco principal é a implementação de regras de negócio complexas com alta confiabilidade, controle de concorrência e integridade de dados.

## 🏗 Arquitetura Monorepo

O projeto utiliza uma estrutura modular Maven para garantir a separação de responsabilidades e facilitar a manutenção:

- **`domain-module`**: Contém as entidades JPA, DTOs e exceções de negócio compartilhadas.
- **`ejb-module`**: O "Core" da aplicação, onde reside a lógica de negócio pesada e o gerenciamento transacional.
- **`backend-module`**: API REST Spring Boot que atua como gateway, orquestrando a integração com o EJB via JNDI/RMI.
- **`frontend/`**: Interface de usuário desenvolvida em Angular 17.

## 🚀 Como Executar o Projeto Completo

### Pré-requisitos

- **Java 17**
- **Maven 3.8+**
- **Node.js 20+** e **npm**
- *Opcional*: **Docker** (apenas para execução de testes de integração com Testcontainers)

### Passo 1: Backend (Java)

O projeto está configurado para utilizar o banco de dados **H2 (em memória)** por padrão, eliminando a necessidade de instalação de banco de dados local para avaliação.

> **Nota:** O driver PostgreSQL está configurado com escopo de teste (`scope=test`), portanto, para rodar com PostgreSQL externo, será necessário ajustar o escopo no POM.

Na raiz do projeto, compile todos os módulos e inicie a API:

```bash
mvn clean install
mvn spring-boot:run -pl backend-module
```

### Passo 2: Frontend (Angular)

Em um novo terminal, acesse a pasta do frontend e inicie o servidor de desenvolvimento:

```bash
cd frontend/beneficio-app
npm install
npm start
```

Acesse a aplicação em: [http://localhost:4200](http://localhost:4200)

## 🧪 Documentação Técnica

Para uma análise profunda sobre as decisões de design (SOLID), controle de concorrência (Optimistic Locking) e detalhes de infraestrutura (JNDI/RMI), consulte o arquivo:

👉 [Análise Técnica](docs/TECHNICAL_ANALYSIS.md)
