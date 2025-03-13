# Catálogo de Peças para Usinagem

[![Licença](https://img.shields.io/badge/license-MIT-blue.svg)](LICENSE)

Um catálogo online de peças para usinagem desenvolvido com Spring Boot, Thymeleaf e Materialize CSS. Permite aos usuários navegar, pesquisar e solicitar orçamentos de forma simplificada.

## Funcionalidades

*   **Navegação por categorias:** Explore os produtos organizados por categorias.
*   **Busca:** Encontre produtos específicos rapidamente.
*   **Carrinho de compras:** Adicione produtos ao carrinho e solicite um orçamento.
*   **Integração com WhatsApp:** Envio automático do pedido para o administrador.
*   **Área administrativa:** Gerenciamento de produtos e categorias (requer autenticação).

## Tecnologias Utilizadas

*   **Backend:**
    *   [Spring Boot](https://spring.io/projects/spring-boot)
    *   Java
*   **Frontend:**
    *   [Thymeleaf](https://www.thymeleaf.org/)
    *   [Materialize CSS](https://materializecss.com/)
    *   HTML
    *   CSS
    *   JavaScript
*   **Banco de Dados:**
    *   MySQL (ou outro banco de dados suportado pelo Spring Data JPA)
*   **Segurança:**
    *   [Spring Security](https://spring.io/projects/spring-security)

## Pré-requisitos

*   [Java Development Kit (JDK) 17 ou superior](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html)
*   [Maven](https://maven.apache.org/download.cgi)
*   [MySQL](https://www.mysql.com/) (ou outro banco de dados de sua preferência)

## Configuração

1.  **Clone o repositório:**

    ```bash
    git clone https://github.com/andreyferraz/catalogo-de-pecas
    ```
2.  **Configure o banco de dados:**

    *   Crie um banco de dados MySQL (ou outro) com o nome de sua preferência.
    *   Altere as configurações de conexão no arquivo `src/main/resources/application.properties` (ou crie um arquivo `application.properties` baseado no `application.properties.example`).
    *   Exemplo:

        ```properties
        spring.datasource.url=jdbc:mysql://localhost:3306/seu_banco?useSSL=false&serverTimezone=UTC
        spring.datasource.username=seu_usuario
        spring.datasource.password=sua_senha
        ```
3.  **Execute a aplicação:**

    ```bash
    cd seu-repositorio
    mvn spring-boot:run
    ```

    A aplicação estará disponível em `http://localhost:8080`.

## Configuração do Ambiente de Desenvolvimento

1.  **Importe o projeto no IntelliJ IDEA (ou sua IDE preferida).**
2.  **Configure um banco de dados MySQL (ou outro) localmente.**
3.  **Crie um arquivo `application.properties` na pasta `src/main/resources` com as configurações do seu banco de dados local.**
4.  **Execute a classe `CatalogoDePecasApplication` para iniciar a aplicação.**

## Acesso à Área Administrativa

1.  Acesse `http://localhost:8080/login`.
2.  Utilize as seguintes credenciais padrão (você pode alterá-las no código):
    *   Usuário: `admin`
    *   Senha: `admin`

## Contribuição

Contribuições são sempre bem-vindas! Sinta-se à vontade para abrir issues e enviar pull requests.

## Licença

Este projeto está licenciado sob a licença [MIT](LICENSE).