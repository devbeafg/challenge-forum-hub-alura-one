# 🧵 ForumHub API

![Java](https://img.shields.io/badge/Java-17-red)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![MySQL](https://img.shields.io/badge/MySQL-Database-blue)
![JWT](https://img.shields.io/badge/Auth-JWT-orange)
![Flyway](https://img.shields.io/badge/Migrations-Flyway-red)
![Status](https://img.shields.io/badge/status-active-success)

## 📚 Sobre o projeto

O **ForumHub** é uma API REST desenvolvida em **Java com Spring Boot** que simula o funcionamento de um fórum de discussão.

A aplicação permite que usuários criem tópicos, respondam discussões e interajam dentro de cursos cadastrados na plataforma.

O projeto foi desenvolvido como parte do **Challenge Back-End do programa Oracle Next Education (ONE) em parceria com a Alura - Grupo 09**, com foco em práticas modernas de desenvolvimento backend.

A API implementa:

- arquitetura em camadas
- autenticação com **JWT**
- persistência em **MySQL**
- versionamento de banco com **Flyway**
- validações de regras de negócio
- endpoints REST seguindo boas práticas
- health-check da aplicação

---

# 🚀 Tecnologias utilizadas

| Tecnologia | Descrição |
|---|---|
Java 17 | Linguagem principal |
Spring Boot | Framework principal |
Spring Security | Segurança da aplicação |
JWT | Autenticação baseada em token |
Spring Data JPA | ORM |
Hibernate | Implementação JPA |
MySQL | Banco de dados relacional |
Flyway | Versionamento de banco |
Maven | Gerenciamento de dependências |

---

# 🧠 Conceito da aplicação

A API simula o backend de um fórum de estudos onde:

- usuários podem criar tópicos
- tópicos pertencem a cursos
- tópicos possuem respostas
- usuários possuem perfis
- acesso à API é protegido por autenticação

---

# 📂 Estrutura do projeto

```bash
forumhub
│
├── src
│ ├── main
│ │ ├── java
│ │ │ └── com.devbeafg.forumhub
│ │ │
│ │ │ ├── controller
│ │ │ │ ├── TopicoController
│ │ │ │ ├── UsuarioController
│ │ │ │ ├── CursoController
│ │ │ │ ├── RespostaController
│ │ │ │ └── AuthController
│ │ │ │
│ │ │ ├── domain
│ │ │ │ ├── model
│ │ │ │ │ ├── Usuario
│ │ │ │ │ ├── Topico
│ │ │ │ │ ├── Curso
│ │ │ │ │ ├── Resposta
│ │ │ │ │ └── Perfil
│ │ │ │ │
│ │ │ │ ├── repository
│ │ │ │ │ ├── UsuarioRepository
│ │ │ │ │ ├── TopicoRepository
│ │ │ │ │ ├── CursoRepository
│ │ │ │ │ └── RespostaRepository
│ │ │ │
│ │ │ ├── dto
│ │ │ │ ├── request
│ │ │ │ └── response
│ │ │ │
│ │ │ ├── security
│ │ │ │ ├── SecurityConfig
│ │ │ │ ├── TokenService
│ │ │ │ └── JwtFilter
│ │ │ │
│ │ │ ├── service
│ │ │ │ └── regras de negócio
│ │ │ │
│ │ │ └── ForumHubApplication
│ │ │
│ │ └── resources
│ │ ├── application.properties
│ │ └── db/migration
│ │ └── scripts Flyway
│ │
│ └── test
│
├── .gitignore
├── pom.xml
├── mvnw
└── README.md
```

---

# ⚙️ Configuração do ambiente

## Pré-requisitos

Antes de iniciar o projeto é necessário ter instalado:

- Java 17+
- Maven
- MySQL
- Git

---

# 🗄️ Configuração do banco de dados

Crie um banco MySQL:

```sql
CREATE DATABASE forumhub_db;
```

🔑 Variáveis de ambiente

Configure as seguintes variáveis no arquivo .env:

```bash
DB_HOST=localhost
DB_PORT=3306
DB_NAME=forumhub_db
DB_USER=seu_usuario
DB_PASSWORD=sua_senha
```

▶️ Executando a aplicação

Clone o projeto

```bash
git clone https://github.com/devbeafg/challenge-forum-hub-alura-one.git
```

Entre na pasta

```bash
cd challenge-forum-hub-alura-one
```

Execute a aplicação

```bash
./mvnw spring-boot:run
```

ou

```bash
mvn spring-boot:run
````

A API estará disponível em:

```bash
http://localhost:8080/swagger-ui/index.html
```


🩺 Health Check

Endpoint de verificação da API:

```bash
GET /healthCheck
```

Resposta esperada:

```bash
{
 "status": "UP",
 "app": "Forumhub",
 "timestamp": "2026-03-10T12:45:00Z"
}
```

🔐 Autenticação

A API utiliza JWT (JSON Web Token).

Login
```bash
POST /login
```
Body
```bash
{
 "email": "usuario@email.com",
 "senha": "123456"
}
```
Resposta
```bash
{
 "token": "jwt_token_aqui"
}
```

Utilize o token no header:

```bash
Authorization: Bearer TOKEN
```

# 📡 Endpoints principais
## 👤 Usuários

| Método |	Endpoint |	Descrição |
| --- | --- | --- |
| GET |	/usuarios |	Lista usuários (paginado) |
| GET |	/usuarios/{id} |	Detalha usuário por ID |
| POST |	/usuarios |	Cria usuário |
| PUT |	/usuarios/{id} |	Atualiza usuário |
| DELETE |	/usuarios/{id} |	Remove usuário |

## 📚 Cursos

| Método |	Endpoint |	Descrição |
| --- | --- | --- |
| GET |	/cursos |	Lista cursos (paginado) |
| GET |	/cursos/{id} |	Detalha curso por ID |
| POST |	/cursos |	Cria curso |
| PUT |	/cursos/{id} |	Atualiza curso |
| DELETE |	/cursos/{id} |	Remove curso |

## 🧩 Perfis

| Método |	Endpoint |	Descrição |
| --- | --- | --- |
| GET |	/perfis |	Lista perfis (paginado) |
| GET |	/perfis/{id} |	Detalha perfil por ID |
| POST |	/perfis |	Cria perfil |
| PUT |	/perfis/{id} |	Atualiza perfil |
| DELETE |	/perfis/{id} |	Remove perfil |

## 🧵 Tópicos

| Método |	Endpoint |	Descrição |
| --- | --- | --- |
| GET |	/topicos |	Lista tópicos (paginado) |
| GET |	/topicos/{id} |	Detalha tópico |
| POST |	/topicos |	Cria tópico |
| PUT |	/topicos/{id} |	Atualiza tópico |
| PUT |	/topicos/{topicoId}/resolver |	Marca tópico como resolvido |
| DELETE |	/topicos/{id} |	Remove tópico |

## 💬 Respostas

| Método |	Endpoint |	Descrição |
| --- | --- | --- |
| GET |	/respostas |	Lista respostas (paginado, aceita `?usuarioId=`) |
| GET |	/respostas/{id} |	Detalha resposta por ID |
| POST |	/respostas |	Cria resposta |
| PUT |	/respostas/{id} |	Atualiza resposta |
| DELETE |	/respostas/{id} |	Remove resposta |

## 🧪 Testando a API

Você pode testar a API utilizando ferramentas como:

- **Postman**
- **Insomnia**
- **Thunder Client**
- **cURL**

Exemplo de requisição:

```bash
curl -X GET "http://localhost:8080/topicos" \
  -H "Authorization: Bearer SEU_TOKEN_AQUI"
```

---

## 🗃️ Versionamento do banco

O projeto utiliza **Flyway** para controle de migrações.

Os scripts ficam em:

```bash
src/main/resources/db/migration
```

Formato dos arquivos:

```text
V1__create_tables.sql
V2__add_relations.sql
...
```

---

## 🛡️ Segurança

A segurança da aplicação é implementada com:

- **Spring Security**
- **JWT**
- filtros de autenticação
- validação de rotas protegidas

Fluxo de segurança:

1. Login
2. Geração do token JWT
3. Envio do token no header `Authorization: Bearer TOKEN`
4. Acesso às rotas protegidas

---

## 📈 Melhorias futuras

- documentação mais detalhada com Swagger / OpenAPI
- paginação nas consultas
- cache com Redis
- dockerização do ambiente
- testes automatizados

---

## 👩‍💻 Autora

Desenvolvido por **Beatriz França Gusmão**.

- GitHub: `https://github.com/devbeafg`

---

## 📜 Licença

Este projeto foi desenvolvido para fins educacionais dentro do programa **Oracle Next Education (ONE)** em parceria com a **Alura**.
