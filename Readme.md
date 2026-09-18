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
* **Java:** Linguagem com tipagem estática robusta, excelente desempenho e confiabilidade em sistemas corporativos.
* **Spring Boot:** Reduz o boilerplate de configuração, provê servidor Tomcat embutido e oferece integração nativa com o ecossistema REST (`Spring MVC` e `Jakarta Validation`).
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

| Método | Rota | Descrição | Status Sucesso |
| :--- | :--- | :--- | :--- |
| `POST` | `/destinos` | Cadastra um novo destino | `201 Created` |
| `GET` | `/destinos` | Lista todos os destinos | `200 OK` |
| `GET` | `/destinos/pesquisar?termo={termo}` | Busca por nome ou localização | `200 OK` |
| `GET` | `/destinos/{id}` | Detalha um destino específico | `200 OK` ou `404 Not Found` |
| `PUT` | `/destinos/{id}` | Atualiza os dados de um destino | `200 OK` ou `404 Not Found` |
| `PATCH` | `/destinos/{id}/avaliar` | Registra nota (1 a 5) e recalcula média | `200 OK` ou `404 Not Found` |
| `DELETE`| `/destinos/{id}` | Remove um destino | `204 No Content` ou `404 Not Found` |

---

## 🚀 Exemplos de Requisições (JSON)

### 1. Cadastrar Destino (`POST /destinos`)
```json
{
  "nome": "Fernando de Noronha",
  "localizacao": "Pernambuco, Brasil",
  "descricao": "Arquipélago vulcânico com praias preservadas e mergulho de alta visibilidade."
}


Comando que utilizamos para os testes de todos os endpoint desta API:

curl.exe -i -X POST http://localhost:8080/destinos -H "Content-Type: application/json" -d '{\"nome\": \"Florianópolis\", \"localizacao\": \"Santa Catarina, Brasil\", \"descricao\": \"Ilha com mais de 40 praias.\"}'


Teste de Cadastro:

curl.exe -i -X POST http://localhost:8080/destinos -H "Content-Type: application/json" -d '{\"nome\": \"Ouro Preto\", \"localizacao\": \"Minas Gerais, Brasil\", \"descricao\": \"Cidade histórica colonial famosa por sua arquitetura barroca, igrejas ricamente decoradas e ladeiras de pedra.\"}'
HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 19:47:42 GMT

{"id":5,"nome":"Ouro Preto","localizacao":"Minas Gerais, Brasil","descricao":"Cidade histórica colonial famosa por sua arquitetura barroca, igrejas ricamente decoradas e ladeiras de pedra.","mediaAvaliacao":0.0,"totalAvaliacoes":0}

Teste de Listagem:

curl.exe -i -X GET http://localhost:8080/destinos

HTTP/1.1 200                                                                                                                 
Content-Type: application/json
Transfer-Encoding: chunked                                                                                                                               
Date: Fri, 28 Aug 2026 19:47:52 GMT                                                                                                           

[{"id":1,"nome":"Florianópolis","localizacao":"Santa Catarina, Brasil","descricao":"Ilha com mais de 40 praias.","mediaAvaliacao":0.0,"totalAvaliacoes":0},{"id":2,"nome":"Santos","localizacao":"São Paulo, Brasil","descricao":"Cidade litorânea.","mediaAvaliacao":0.0,"totalAvaliacoes":0},{"id":3,"nome":"Fernando de Noronha","localizacao":"Pernambuco, Brasil","descricao":"Arquipélago vulcânico isolado, famoso por suas praias paradisíacas, águas cristalinas e rica vida marinha.","mediaAvaliacao":0.0,"totalAvaliacoes":0},{"id":4,"nome":"Gramado","localizacao":"Rio Grande do Sul, Brasil","descricao":"Cidade serrana com forte influência alemã e italiana, conhecida pelo clima frio, arquitetura europeia e festivais.","mediaAvaliacao":0.0,"totalAvaliacoes":0},{"id":5,"nome":"Ouro Preto","localizacao":"Minas Gerais, Brasil","descricao":"Cidade histórica colonial famosa por sua arquitetura barroca, igrejas ricamente decoradas e ladeiras de pedra.","mediaAvaliacao":0.0,"totalAvaliacoes":0}]

Teste de busca:

curl.exe -i -X GET "http://localhost:8080/destinos/pesquisar?termo=Ouro"

[{"id":5,"nome":"Ouro Preto","localizacao":"Minas Gerais, Brasil","descricao":"Cidade histórica colonial famosa por sua arquitetura barroca, igrejas ricamente decoradas e ladeiras de pedra.","mediaAvaliacao":0.0,"totalAvaliacoes":0}]

Teste de Detalhamento de Destino:

curl.exe -i -X GET "http://localhost:8080/destinos/{3}"                 
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 19:55:52 GMT

{"id":3,"nome":"Fernando de Noronha","localizacao":"Pernambuco, Brasil","descricao":"Arquipélago vulcânico isolado, famoso por suas praias paradisíacas, águas cristalinas e rica vida marinha.","mediaAvaliacao":0.0,"totalAvaliacoes":0}

Teste de Atualização de Destino:

curl.exe -i -X PUT "http://localhost:8080/destinos/{1}" -H "Content-Type: application/json" -d '{\"nome\": \"Florianópolis\", \"localizacao\": \"Santa Catarina, Brasil\", \"descricao\": \"Ilha da Magia, famosa por unir praias paradisíacas, natureza preservada e a infraestrutura de uma grande capital.\"}' 

HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 20:01:06 GMT

{"id":1,"nome":"Florianópolis","localizacao":"Santa Catarina, Brasil","descricao":"Ilha da Magia, famosa por unir praias paradisíacas, natureza preservada e a infraestrutura de uma grande capital.","mediaAvaliacao":0.0,"totalAvaliacoes":0}


Teste de Avaliação:

curl.exe -i -X PATCH http://localhost:8080/destinos/1/avaliar -H "Content-Type: application/json" -d '{\"nota\": 5}'
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 20:06:33 GMT

{"id":1,"nome":"Florianópolis","localizacao":"Santa Catarina, Brasil","descricao":"Ilha da Magia, famosa por unir praias paradisíacas, natureza preservada e a infraestrutura de uma grande capital.","mediaAvaliacao":5.0,"totalAvaliacoes":1}


Teste de Média de Avaliações:

curl.exe -i -X PATCH http://localhost:8080/destinos/3/avaliar -H "Content-Type: application/json" -d '{\"nota\": 5}'
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 20:08:26 GMT

{"id":3,"nome":"Fernando de Noronha","localizacao":"Pernambuco, Brasil","descricao":"Arquipélago vulcânico isolado, famoso por suas praias paradisíacas, águas cristalinas e rica vida marinha.","mediaAvaliacao":5.0,"totalAvaliacoes":1}
PS C:\Users\e015614\Desktop\Documentos Pessoais\ADS EAD - SENAI\2026-02-Quarto Semestre\2-Desenvolvimento de Sistemas Web\destinos-api> curl.exe -i -X PATCH http://localhost:8080/destinos/3/avaliar -H "Content-Type: application/json" -d '{\"nota\": 4}'
HTTP/1.1 200 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 20:08:39 GMT

{"id":3,"nome":"Fernando de Noronha","localizacao":"Pernambuco, Brasil","descricao":"Arquipélago vulcânico isolado, famoso por suas praias paradisíacas, águas cristalinas e rica vida marinha.","mediaAvaliacao":4.5,"totalAvaliacoes":2}
PS C:\Users\e015614\Desktop\Documentos Pessoais\ADS EAD - SENAI\2026-02-Quarto Semestre\2-Desenvolvimento de Sistemas Web\destinos-api> 


Teste de cadastro e exclusão de destino:


curl.exe -i -X POST http://localhost:8080/destinos -H "Content-Type: application/json" -d '{\"nome\": \"Teste\", \"localizacao\": \"Estado, Brasil\", \"descricao\": \"Descriçãos.\"}'                                
HTTP/1.1 201 
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 20:13:11 GMT

{"id":6,"nome":"Teste","localizacao":"Estado, Brasil","descricao":"Descriçãos.","mediaAvaliacao":0.0,"totalAvaliacoes":0}

curl.exe -i -X DELETE "http://localhost:8080/destinos/{6}"
HTTP/1.1 204 
Date: Fri, 28 Aug 2026 20:15:41 GMT


 curl.exe -i -X GET http://localhost:8080/destinos                                                                                                                         
HTTP/1.1 200                                                                                                                            
Content-Type: application/json
Transfer-Encoding: chunked
Date: Fri, 28 Aug 2026 20:16:29 GMT

[{"id":1,"nome":"Florianópolis","localizacao":"Santa Catarina, Brasil","descricao":"Ilha da Magia, famosa por unir praias paradisíacas, natureza preservada e a infraestrutura de uma grande capital.","mediaAvaliacao":5.0,"totalAvaliacoes":1},{"id":2,"nome":"Santos","localizacao":"São Paulo, Brasil","descricao":"Cidade litorânea.","mediaAvaliacao":4.0,"totalAvaliacoes":1},{"id":3,"nome":"Fernando de Noronha","localizacao":"Pernambuco, Brasil","descricao":"Arquipélago vulcânico isolado, famoso por suas praias paradisíacas, águas cristalinas e rica vida marinha.","mediaAvaliacao":4.5,"totalAvaliacoes":2},{"id":4,"nome":"Gramado","localizacao":"Rio Grande do Sul, Brasil","descricao":"Cidade serrana com forte influência alemã e italiana, conhecida pelo clima frio, arquitetura europeia e festivais.","mediaAvaliacao":0.0,"totalAvaliacoes":0},{"id":5,"nome":"Ouro Preto","localizacao":"Minas Gerais, Brasil","descricao":"Cidade histórica colonial famosa por sua arquitetura barroca, igrejas ricamente decoradas e ladeiras de pedra.","mediaAvaliacao":0.0,"totalAvaliacoes":0}]
