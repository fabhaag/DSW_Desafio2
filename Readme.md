# 🌍 API REST - Gestão de Destinos de Viagem

**UC:** Desenvolvimento de Sistemas Web  
**Tutor:** Julio Cezar Rutke  
**Desafio 2:** Evolução da API REST com banco de dados e segurança   
**Grupo:** 8  
**Nome:** Fabiano Carcuchinski Haag  

A API Destinos é uma aplicação desenvolvida em Java com Spring Boot para o gerenciamento de destinos turísticos. Esta versão representa uma evolução arquitetural, migrando o armazenamento de dados em memória para um banco de dados relacional PostgreSQL utilizando Spring Data JPA. Além disso, a aplicação agora conta com mecanismos de autenticação e autorização baseados em perfis de acesso (ADMIN e USER) implementados com Spring Security.

---

## 🏛️ Arquitetura e Decisões Técnicas

A aplicação adota uma **Arquitetura em Camadas (Layered Architecture)**, garantindo separação clara de responsabilidades, alta coesão e facilidade de manutenção futura:

* **Config (`config`):** Centraliza arquivos como `SecurityConfig.java`, configurações de CORS (Cross-Origin Resource Sharing), Swagger/OpenAPI ou configurações de cache em um pacote `config`, ajudando a manter a arquitetura limpa. Isso separa as regras de negócio e o mapeamento do banco de dados das configurações de infraestrutura e comportamento do framework.
* **Controller (`controller`):** Responsável pelo tratamento das requisições HTTP, validação de payload via Bean Validation e retorno de status codes apropriados.
* **Service (`service`):** Camada de regras de negócio, centralizando a lógica de cadastro, pesquisa filtrada, exclusão e recálculo da média ponderada de avaliações.
* **Model/Entity (`model`):** Representação do domínio (`Destino`), encapsulando os atributos essenciais e métodos de negócio sobre seu próprio estado.
* **Repository (`repository`):** A pasta repository isola as interfaces responsáveis pela comunicação direta com o banco de dados (Spring Data JPA).
* **DTO (`dto`):** Objetos de transferência de dados (`Record`) para desacoplar a entrada de requisições do modelo interno.
* **Persistência em Banco:** Java, banco de dados PostgreSQL rodando na porta 5432 e as credenciais (`admin/1234` e `user/1234`) geradas pelo inicializador de dados para que os avaliadores consigam testar o controle de permissões por perfil de acesso adequadamente.

---

## 🛠️ Tecnologias Utilizadas e Justificativas

### 🚀 O Core e Ecossistema Base
* **Java:** É a linguagem de programação base. Orientada a objetos, segura, independente de plataforma (roda em qualquer lugar através da JVM) e amplamente utilizada por grandes empresas devido à sua estabilidade e performance.
* **Spring Boot:** Framework que estende o ecossistema Spring tradicional. Seu papel principal é simplificar a configuração, eliminando a necessidade de configurações manuais complexas. Traz um servidor web embutido (Tomcat), permitindo rodar a aplicação web imediatamente.

### 🗄️ Persistência e Banco de Dados
* **Spring Data JPA:** Módulo do Spring que facilita a comunicação com o banco de dados via ORM (Mapeamento Objeto-Relacional), permitindo interagir com as tabelas usando classes e métodos Java comuns.
* **PostgreSQL:** Sistema de gerenciamento de banco de dados relacional (SGBD) de código aberto, poderoso, seguro e conhecido por aguentar grandes volumes de dados e consultas complexas.

### 🛡️ Segurança
* **Spring Security:** Módulo responsável por proteger a aplicação, gerenciando autenticação (login com usuário e senha) e autorização (verificar perfis como ADMIN e USER).

### 📦 Gerenciamento do Projeto e Editor
* **Maven:** Ferramenta de automação de build e gerenciamento de dependências (`pom.xml`).
* **Visual Studio Code:** Editor leve, com suporte a extensões como *Extension Pack for Java* e *Spring Boot Extension Pack*.

### 📊 Resumo Visual: Como as tecnologias trabalham juntas

```text
┌───────────────────────────────────────────────────────────┐
│                          MAVEN                            │
│           (Gerencia e monta todo o projeto)               │
└─────────────────────────────┬─────────────────────────────┘
                              ▼
┌───────────────────────────────────────────────────────────┐
│                       JAVA + SPRING BOOT                  │
│             (Motor principal e lógica de negócio)         │
│                                                           │
│   ┌───────────────────┐           ┌───────────────────┐   │
│   │  SPRING SECURITY  │           │  SPRING DATA JPA  │   │
│   │ (Protege os dados │           │ (Conversa com o   │   │
│   │    e acessos)     │           │   banco em Java)  │   │
│   └───────────────────┘           └─────────┬─────────┘   │
└─────────────────────────────────────────────┼─────────────┘
                                              ▼
                                    ┌───────────────────┐
                                    │    POSTGRESQL     │
                                    │ (Guarda os dados) │
                                    └───────────────────┘
```

---

## 🔌 Endpoints da API

| Método | Rota | Descrição | Status Sucesso | Permissão |
| :--- | :--- | :--- | :--- | :--- |
| `POST` | `/destinos` | Cadastra um novo destino | `201 Created` | **ADMIN** |
| `GET` | `/destinos` | Lista todos os destinos | `200 OK` | **Público** |
| `GET` | `/destinos/pesquisar?termo={termo}` | Busca por nome ou localização | `200 OK` | **Público** |
| `GET` | `/destinos/{id}` | Detalha um destino específico | `200 OK` ou `404 Not Found` | **Público** |
| `PUT` | `/destinos/{id}` | Atualiza os dados de um destino | `200 OK` ou `404 Not Found` | **ADMIN** |
| `PATCH` | `/destinos/{id}/avaliar` | Registra nota (1 a 5) e recalcula média | `200 OK` ou `404 Not Found` | **ADMIN, USER** |
| `DELETE`| `/destinos/{id}` | Remove um destino | `204 No Content` ou `404 Not Found` | **ADMIN** |

---

## 🖥️ Configuração do Servidor (Amazon EC2)

Foi utilizado um servidor Amazon EC2 com IP Elástico (`54.80.166.128`), rodando na porta `8080`. Abaixo estão os comandos utilizados para configurar o ambiente e o banco de dados PostgreSQL.

```bash
# Atualizar os pacotes
sudo dnf update -y

# Instalar o Java 21 (Amazon Corretto)
sudo dnf install java-21-amazon-corretto -y

# Instalar o Git
sudo dnf install git -y

# Verificar se o Java está no path corretamente
java -version

# Instalar o servidor PostgreSQL
sudo dnf install postgresql15-server -y

# Inicializar o banco de dados
sudo postgresql-setup --initdb

# Iniciar e habilitar o serviço para iniciar com o sistema
sudo systemctl start postgresql
sudo systemctl enable postgresql

# Acessar o terminal do PostgreSQL para criar o banco e configurar a senha do usuário padrão:
sudo -u postgres psql

CREATE DATABASE destinos_db;
ALTER USER postgres WITH PASSWORD '1234';
\q

# Clonar o repositório
git clone https://github.com/fabhaag/DSW_Desafio2.git
cd DSW_Desafio2

# Se necessário, edite o application.properties para bater com a senha do banco
nano src/main/resources/application.properties

# Dar permissão de execução ao Maven Wrapper
chmod +x mvnw

# Iniciar a aplicação
./mvnw spring-boot:run

# Verificar o status do PostgreSQL (opcional)
systemctl status postgresql

# Revisar as credenciais no application.properties (opcional)
cat src/main/resources/application.properties
```

---

## 🚀 Testes de Requisições (Postman)

Os testes foram realizados utilizando o **Postman**.  
Endpoint base: `http://54.80.166.128:8080/destinos`

*   **Authorization:** Selecionar **Basic Auth**.  
*   **Body:** Selecionar **raw** e o formato **JSON**.

### 1. Cadastrar Destino (`POST /destinos`)
*   **Credenciais:** `admin` / `1234`

**Corpo da Requisição:**
```json
{
  "nome": "Praia da Joaquina",
  "localizacao": "Florianópolis, SC",
  "descricao": "Famosa pelas dunas e surf."
}
```

**Resposta:** `201 Created`
```json
[
    {
        "nome": "Praia da Joaquina",
        "localizacao": "Florianópolis, SC",
        "descricao": "Famosa pelas dunas e surf.",
        "id": 1,
        "mediaAvaliacao": 0.0,
        "totalAvaliacoes": 0
    }
]
```

### 2. Tentativa de cadastrar Destino com usuário comum (`POST /destinos`)
*   **Credenciais:** `user` / `1234`

**Corpo da Requisição:**
```json
{
  "nome": "Fernando de Noronha",
  "localizacao": "Pernambuco, Brasil",
  "descricao": "Arquipélago vulcânico isolado, famoso por suas praias paradisíacas"
}
```

**Resposta:** `403 Forbidden`
```json
{
    "timestamp": "2026-09-18T20:56:53.131Z",
    "status": 403,
    "error": "Forbidden",
    "path": "/destinos"
}
```

### 3. Recuperar Destinos (`GET /destinos`)
*   **Credenciais:** `admin` / `1234` (Ou sem autenticação, dependendo da configuração)

**Resposta:** `200 OK`
```json
[
    {
        "nome": "Praia da Joaquina",
        "localizacao": "Florianópolis, SC",
        "descricao": "Famosa pelas dunas e surf.",
        "id": 1,
        "mediaAvaliacao": 0.0,
        "totalAvaliacoes": 0
    },
    {
        "nome": "Fernando de Noronha",
        "localizacao": "Pernambuco, Brasil",
        "descricao": "Arquipélago vulcânico isolado, famoso por suas praias paradisíacas",
        "id": 2,
        "mediaAvaliacao": 0.0,
        "totalAvaliacoes": 0
    }
]
```

### 4. Buscar Destinos (`GET /destinos/pesquisar?termo=FLO`)
*   **Credenciais:** `admin` / `1234`

**Resposta:** `200 OK`
```json
[
    {
        "nome": "Praia da Joaquina",
        "localizacao": "Florianópolis, SC",
        "descricao": "Famosa pelas dunas e surf.",
        "id": 1,
        "mediaAvaliacao": 0.0,
        "totalAvaliacoes": 0
    }
]
```

### 5. Detalhar Destino (`GET /destinos/2`)
*   **Credenciais:** `admin` / `1234`

**Resposta:** `200 OK`
```json
{
    "nome": "Fernando de Noronha",
    "localizacao": "Pernambuco, Brasil",
    "descricao": "Arquipélago vulcânico isolado, famoso por suas praias paradisíacas",
    "id": 2,
    "mediaAvaliacao": 0.0,
    "totalAvaliacoes": 0
}
```

### 6. Tentativa de Atualizar Destino com usuário comum (`PUT /destinos/2`)
*   **Credenciais:** `user` / `1234`

**Corpo da Requisição:**
```json
{
  "nome": "Fernando de Noronha",
  "localizacao": "Pernambuco, Brasil",
  "descricao": "Arquipélago vulcânico"
}
```

**Resposta:** `403 Forbidden`
```json
{
    "timestamp": "2026-09-18T21:12:09.983Z",
    "status": 403,
    "error": "Forbidden",
    "path": "/destinos/2"
}
```

### 7. Atualizar Destino como Admin (`PUT /destinos/2`)
*   **Credenciais:** `admin` / `1234`

**Corpo da Requisição:**
```json
{
  "nome": "Fernando de Noronha",
  "localizacao": "Pernambuco, Brasil",
  "descricao": "Arquipélago vulcânico"
}
```

**Resposta:** `200 OK`
```json
{
    "nome": "Fernando de Noronha",
    "localizacao": "Pernambuco, Brasil",
    "descricao": "Arquipélago vulcânico",
    "id": 2,
    "mediaAvaliacao": 0.0,
    "totalAvaliacoes": 0
}
```

### 8. Avaliar Destino (`PATCH /destinos/1/avaliar`)
*   **Credenciais:** `admin` / `1234`

**Corpo da Requisição:**
```json
{
  "nota": "4"
}
```

**Resposta:** `200 OK`
```json
{
    "nome": "Praia da Joaquina",
    "localizacao": "Florianópolis, SC",
    "descricao": "Famosa pelas dunas e surf.",
    "id": 1,
    "mediaAvaliacao": 4.0,
    "totalAvaliacoes": 1
}
```

### 9. Cadastrar Novo Destino (`POST /destinos`)
*   **Credenciais:** `admin` / `1234`

**Corpo da Requisição:**
```json
{
  "nome": "Ouro Preto",
  "localizacao": "Minas Gerais, Brasil",
  "descricao": "Cidade histórica colonial famosa por sua arquitetura barroca"
}
```

**Resposta:** `201 Created`
```json
{
    "nome": "Ouro Preto",
    "localizacao": "Minas Gerais, Brasil",
    "descricao": "Cidade histórica colonial famosa por sua arquitetura barroca",
    "id": 3,
    "mediaAvaliacao": 0.0,
    "totalAvaliacoes": 0
}
```

### 10. Deletar Destino (`DELETE /destinos/3`)
*   **Credenciais:** `admin` / `1234`

**Resposta:** `204 No Content` *(Sem corpo de resposta)*