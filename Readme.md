# 🌍 API REST - Gestão de Destinos de Viagem

UC:  Desenvolvimento de Sistemas Web
Tutor: Julio Cezar Rutke
Desafio 2: Evolução da API REST com banco de dados e segurança 
Grupo: 8
Nomes: Fabiano Carcuchinski Haag


A API Destinos é uma aplicação desenvolvida em Java com Spring Boot para o gerenciamento de destinos turísticos. Esta versão representa uma evolução arquitetural, migrando o armazenamento de dados em memória para um banco de dados relacional PostgreSQL utilizando Spring Data JPA. Além disso, a aplicação agora conta com mecanismos de autenticação e autorização baseados em perfis de acesso (ADMIN e USER) implementados com Spring Security.

---

## 🏛️ Arquitetura e Decisões Técnicas

A aplicação adota uma **Arquitetura em Camadas (Layered Architecture)**, garantindo separação clara de responsabilidades, alta coesão e facilidade de manutenção futura:

* **Config (`config`):** Centralizar arquivos como SecurityConfig.java, configurações de CORS (Cross-Origin Resource Sharing), Swagger/OpenAPI ou configurações de cache em um pacote config ajuda a manter a arquitetura limpa. Isso separa as regras de negócio e o mapeamento do banco de dados (que ficam em service e model) das configurações de infraestrutura e comportamento do framework.
* **Controller (`controller`):** Responsável pelo tratamento das requisições HTTP, validação de payload via Bean Validation e retorno de status codes apropriados.
* **Service (`service`):** Camada de regras de negócio, centralizando a lógica de cadastro, pesquisa filtrada, exclusão e recálculo da média ponderada de avaliações.
* **Model/Entity (`model`):** Representação do domínio (`Destino`), encapsulando os atributos essenciais e métodos de negócio sobre seu próprio estado.
* **Repository (`repository`):** A Pasta repository isola as interfaces responsáveis pela comunicação direta com o banco de dados (Spring Data JPA).
* **DTO (`dto`):** Objetos de transferência de dados (`Record`) para desacoplar a entrada de requisições do modelo interno.
* **Persistência em Banco:** Java, banco de dados PostgreSQL rodando na porta 5432 e as credenciais (admin/1234 e user/1234) geradas pelo inicializador de dados para que os avaliadores consigam testar o controle de permissões por perfil de acesso adequadamente.



### Justificativa das Tecnologias
* **Java:** 
* **Spring Boot:** 
* **Spring Boot JPA:** 
* **Spring Security:** 
* **PostgreSQL:** 
* **Maven:** 


Editor:
* **Visual Studio Code:** Editor leve, com suporte a extensões como *Extension Pack for Java* e *Spring Boot Extension Pack*.

🚀 O Core e Ecossistema Base

* **Java:** É a linguagem de programação base. É uma linguagem orientada a objetos, segura, independente de plataforma (roda em qualquer lugar através da JVM) e amplamente utilizada por grandes empresas devido à sua estabilidade e performance.

* **Spring Boot:** É um framework que estende o ecossistema Spring tradicional. O seu papel principal é simplificar a configuração. Ele elimina a necessidade de configurações manuais complexas (como arquivos XML gigantes) e traz um servidor web embutido (como o Tomcat). Isso significa que você pode rodar sua aplicação web imediatamente como um programa Java comum.

🗄️ Persistência e Banco de Dados

* **Spring Data JPA**: É um módulo do Spring que facilita drasticamente a comunicação com o banco de dados. Ele utiliza o conceito de ORM (Mapeamento Objeto-Relacional), permitindo que você interaja com as tabelas do banco de dados usando classes e métodos Java comuns, sem precisar escrever códigos SQL manuais para operações básicas (como salvar, deletar ou buscar).

* **PostgreSQL**: É o banco de dados propriamente dito. Trata-se de um sistema de gerenciamento de banco de dados relacional (SGBD) de código aberto, extremamente poderoso, seguro e conhecido por aguentar grandes volumes de dados e consultas complexas com excelente performance.

🛡️ Segurança

* **Spring Security**: É o módulo responsável por proteger a sua aplicação. Ele gerencia os processos de autenticação (verificar quem é o usuário, ex: login com e-mail e senha / tokens JWT) e autorização (verificar o que esse usuário pode fazer, ex: definir que apenas usuários com a regra ADMIN podem deletar um registro).

📦 Gerenciamento do Projeto

* **Maven**: É a ferramenta de automação de build e gerenciamento de dependências. Em vez de baixar arquivos .jar manualmente na internet e colocá-los no projeto, você apenas lista o que precisa (como o driver do PostgreSQL ou o Spring Security) em um arquivo chamado pom.xml. O Maven se encarrega de baixar tudo automaticamente de um repositório central, além de compilar e empacotar o seu projeto para produção.

Resumo Visual: Como as tecnologias trabalham juntas:

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
---

## 🔌 Endpoints da API

// Apenas ADMIN pode cadastrar, atualizar ou excluir
// ADMIN e USER podem listar e avaliar destinos

| Método | Rota | Descrição | Status Sucesso |
| :--- | :--- | :--- | :--- |

| `POST` | `/destinos` | Cadastra um novo destino | `201 Created` |
| Permitido para ADMIN

| `GET` | `/destinos` | Lista todos os destinos | `200 OK` |
| Permitido para todos usuários ou  as Consultas são públicas

| `GET` | `/destinos/pesquisar?termo={termo}` | Busca por nome ou localização | `200 OK` |
| Permitido para todos usuários ou  as Consultas são públicas

| `GET` | `/destinos/{id}` | Detalha um destino específico | `200 OK` ou `404 Not Found` |
| Permitido para todos usuários ou  as Consultas são públicas

| `PUT` | `/destinos/{id}` | Atualiza os dados de um destino | `200 OK` ou `404 Not Found` |
Permitida para "ADMIN"

| `PATCH` | `/destinos/{id}/avaliar` | Registra nota (1 a 5) e recalcula média | `200 OK` ou `404 Not Found` |
Avaliação permitida para "USER" ou "ADMIN"

| `DELETE`| `/destinos/{id}` | Remove um destino | `204 No Content` ou `404 Not Found` |
Permitida para "ADMIN"

---

## 🚀 Exemplos de Requisições (JSON)

Os testes foram realizados com o POSTMAN, nos campos 

AUTORIZATION 
foi selecionado "Basic Auth", Username: "admin" e Password: "1234".

BODY foi selecionado "Raw" e "JSON" com o conteúdo como abaixo:
{
  "nome": "Praia da Joaquina",
  "localizacao": "Florianópolis, SC",
  "descricao": "Famosa pelas dunas e surf."
}

Foi utilizado um servidor Amazon EC2, rodando na porta 8080, com IP Elástico (54.80.166.128:8080) e foi instalado o banco PostgreSQL, os comandos de instalação estão listados abaixo:

As requisições foram para a seguinte endpoint: 

http://54.80.166.128:8080/destinos


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

# Clonar o repositório enviado anteriormente
git clone https://github.com/fabhaag/DSW_Desafio2.git
cd DSW_Desafio2

# Se necessário, edite o application.properties para bater com a senha do banco definida no passo anterior
# nano src/main/resources/application.properties

# Dar permissão de execução ao Maven Wrapper
chmod +x mvnw

# Iniciar a aplicação
./mvnw spring-boot:run


#Verificar o status do PostgreSQL
Systemctl status postgresql

#Revisar as credenciais no application.properties
cat src/main/resources/application.properties


### 1. Cadastrar Destino (`POST /destinos`)

http://54.80.166.128:8080/destinos
"Basic Auth", Username: "admin" e Password: "1234"

```json
{
  "nome": "Praia da Joaquina",
  "localizacao": "Florianópolis, SC",
  "descricao": "Famosa pelas dunas e surf."
}

HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 18 Set 2026 19:47:42 GMT

### 2. Tentativa de cadastrar Destino (`POST /destinos`) com usuário "user"

http://54.80.166.128:8080/destinos
"Basic Auth", Username: "user" e Password: "1234"

{
  "nome": "Fernando de Noronha",
  "localizacao": "Pernambuco, Brasil",
  "descricao": "Arquipélago vulcânico isolado, famoso por suas praias paradisíacas"
}

Resposta: 403 Forbidden

```json
{
    "timestamp": "2026-09-18T20:56:53.131Z",
    "status": 403,
    "error": "Forbidden",
    "path": "/destinos"
}

HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 18 Set 2026 19:47:42 GMT


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

### 3. Recuperar Destinos (`GET /destinos`)
http://54.80.166.128:8080/destinos
"Basic Auth", Username: "admin" e Password: "1234"

Resposta: 200 OK

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 18 Sep 2026 20:39:45 GMT

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

### 4. Buscar Destinos (`GET /destinos`)
"Basic Auth", Username: "admin" e Password: "1234"

http://54.80.166.128:8080/destinos/pesquisar?termo=FLO

Resposta: 200 OK

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

### 5. Detalhar Destinos (`GET /destinos`)
"Basic Auth", Username: "admin" e Password: "1234"

http://54.80.166.128:8080/destinos/2

Resposta: 200 OK

{
    "nome": "Fernando de Noronha",
    "localizacao": "Pernambuco, Brasil",
    "descricao": "Arquipélago vulcânico isolado, famoso por suas praias paradisíacas",
    "id": 2,
    "mediaAvaliacao": 0.0,
    "totalAvaliacoes": 0
}

### 6. Atualizar Destino (`PUT /destinos`)
"Basic Auth", Username: "user" e Password: "1234"

http://54.80.166.128:8080/destinos/2

{
  "nome": "Fernando de Noronha",
  "localizacao": "Pernambuco, Brasil",
  "descricao": "Arquipélago vulcânico"
}

Resposta: 403 Forbidden

{
    "timestamp": "2026-09-18T21:12:09.983Z",
    "status": 403,
    "error": "Forbidden",
    "path": "/destinos/2"
}

### 7. Atualizar Destino (`PUT /destinos`)
"Basic Auth", Username: "admin" e Password: "1234"

http://54.80.166.128:8080/destinos/2

{
  "nome": "Fernando de Noronha",
  "localizacao": "Pernambuco, Brasil",
  "descricao": "Arquipélago vulcânico"
}

Resposta: 200 OK

{
    "nome": "Fernando de Noronha",
    "localizacao": "Pernambuco, Brasil",
    "descricao": "Arquipélago vulcânico",
    "id": 2,
    "mediaAvaliacao": 0.0,
    "totalAvaliacoes": 0
}

### 8. Atualizar Destino (`PATCH /destinos`)
"Basic Auth", Username: "admin" e Password: "1234"

http://54.80.166.128:8080/destinos/1/avaliar

{
  "nota": "4"
}

Resposta: 200 OK

{
    "nome": "Praia da Joaquina",
    "localizacao": "Florianópolis, SC",
    "descricao": "Famosa pelas dunas e surf.",
    "id": 1,
    "mediaAvaliacao": 4.0,
    "totalAvaliacoes": 1
}

### 9. Cadastrar Destino (`POST /destinos`)

http://54.80.166.128:8080/destinos
"Basic Auth", Username: "admin" e Password: "1234"

```json
{
  "nome": "Ouro Preto",
  "localizacao": "Minas Gerais, Brasil",
  "descricao": "Cidade histórica colonial famosa por sua arquitetura barroca"
}

Resposta: 201 Created

{
    "nome": "Ouro Preto",
    "localizacao": "Minas Gerais, Brasil",
    "descricao": "Cidade histórica colonial famosa por sua arquitetura barroca",
    "id": 3,
    "mediaAvaliacao": 0.0,
    "totalAvaliacoes": 0
}

### 10. Delete Destino (`DELETE /destinos`)
"Basic Auth", Username: "admin" e Password: "1234"

http://54.80.166.128:8080/destinos/3

Resposta: 204 No Content


