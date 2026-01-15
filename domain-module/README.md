# Domain Module - Modelo de Dados e Contratos Compartilhados

Este módulo fornece a base de dados e os contratos utilizados por todos os outros módulos do projeto. Ele garante que a "linguagem onipresente" do sistema seja consistente.

## 📦 Conteúdo do Módulo

- **Entidades JPA**: Mapeamento objeto-relacional com suporte a auditoria e controle de versão (`@Version`).
- **Business Exceptions**: Definição de exceções de negócio que disparam o rollback transacional.
- **DTOs**: Records Java para transferência de dados eficiente e imutável.

## ⚙️ Build e Dependência

Este módulo é uma dependência passiva. Para que outros módulos o reconheçam após alterações:

```bash
mvn install -pl domain-module
```
