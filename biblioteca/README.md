# API de Gestão de Biblioteca

API REST desenvolvida em Java com Spring Boot para gerenciar os recursos de uma biblioteca, como livros, autores,
categorias e empréstimos.

## Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3**
- **Spring Web:** Para a criação dos endpoints REST.
- **Spring Data JPA:** Para a persistência de dados.
- **Spring Security:** Para autenticação e autorização via JWT.
- **H2 Database:** Banco de dados em memória para ambiente de desenvolvimento.
- **Lombok:** Para reduzir código boilerplate.
- **Maven:** Para gerenciamento de dependências e build.
- **Swagger (OpenAPI):** Para documentação interativa da API.
- **JUnit 5 & Mockito:** Para testes unitários e de integração.

## Como Executar

### Pré-requisitos

- Java 17 ou superior.
- Maven 3.8 ou superior.

### Passos para Execução

1. **Clone o repositório:**
   ```bash
   git clone <url-do-seu-repositorio>
   cd biblioteca-api
   ```

2. **Execute a aplicação com o Maven:**
   ```bash
   ./mvnw spring-boot:run
   ```
   A API estará disponível em `http://localhost:8080`.

### Acesso ao Banco de Dados (H2)

- **URL do Console H2:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User Name:** `sa`
- **Password:** (deixe em branco)

## Documentação da API (Swagger)

A documentação completa e interativa da API, gerada com Swagger, está disponível em:

- **Swagger UI:** `http://localhost:8080/swagger-ui.html`

## Guia da API

### Autenticação

A maioria dos endpoints que modificam dados (`POST`, `PUT`, `DELETE`) são protegidos e exigem um token JWT.

**1. Obter um Token:**

Faça uma requisição `POST` para o endpoint `/login` com as credenciais de um usuário. Os usuários padrão criados no
início da aplicação são:

- **Admin:** `admin@email.com` / `123`
- **Cliente:** `ana@email.com` / `123`

**Exemplo com cURL:**

```bash
curl -X POST http://localhost:8080/login \
-H "Content-Type: application/json" \
-d '{"email": "admin@email.com", "senha": "123"}' -v
```

A resposta conterá o token no cabeçalho `Authorization`. Ex: `Authorization: Bearer <seu-token-jwt>`.

**2. Usar o Token:**

Inclua o token obtido em todas as requisições subsequentes no cabeçalho `Authorization`.

```bash
Authorization: Bearer <seu-token-jwt>
```

### Exemplos de Uso (cURL)

#### Criar um novo autor (requer token de ADMIN)

```bash
curl -X POST http://localhost:8080/autores \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <seu-token-jwt>" \
-d '{"nome": "Ursula K. Le Guin", "dataNascimento": "1929-10-21", "biografia": "Escritora norte-americana de ficção especulativa."}'
```

#### Listar todos os livros (público)

```bash
curl -X GET http://localhost:8080/livros
```

#### Criar um novo empréstimo (requer token)

```bash
curl -X POST http://localhost:8080/emprestimos \
-H "Content-Type: application/json" \
-H "Authorization: Bearer <seu-token-jwt>" \
-d '{"livroId": 1, "usuarioId": 2}'
```

#### Verificar o status de um empréstimo (requer token)

```bash
curl -X GET http://localhost:8080/emprestimos/1/status \
-H "Authorization: Bearer <seu-token-jwt>"
