# 🌍 API REST - Gestão de Destinos de Viagem

UC:  Desenvolvimento de Sistemas Web
Tutor: Julio Cezar Rutke
Desafio 1: Planejamento da arquitetura e desenvolvimento inicial de API REST 
Grupo: 8
Nomes: Fabiano Carcuchinski Haag


Está é a primeira versão funcional da API RESTful desenvolvida para modernização dos serviços digitais da agência de viagens, permitindo integração com parceiros comerciais e aplicativos de turismo. Esta versão ainda não possui persistência em banco de dados, mas permite o cadastro e listagem de destinos e avaliações dos destinos

---

## 🏛️ Arquitetura e Decisões Técnicas

A aplicação adota uma **Arquitetura em Camadas (Layered Architecture)**, garantindo separação clara de responsabilidades, alta coesão e facilidade de manutenção futura:

* **Controller (`controller`):** Responsável pelo tratamento das requisições HTTP, validação de payload via Bean Validation e retorno de status codes apropriados.
* **Service (`service`):** Camada de regras de negócio, centralizando a lógica de cadastro, pesquisa filtrada, exclusão e recálculo da média ponderada de avaliações.
* **Model/Entity (`model`):** Representação do domínio (`Destino`), encapsulando os atributos essenciais e métodos de negócio sobre seu próprio estado.
* **DTO (`dto`):** Objetos de transferência de dados (`Record`) para desacoplar a entrada de requisições do modelo interno.
* **Persistência em Memória:** Implementada com `ConcurrentHashMap` e `AtomicLong`, assegurando integridade concorrente (thread-safety) sem a necessidade de infraestrutura de banco de dados neste estágio inicial.

### Justificativa das Tecnologias
* **Java:** Linguagem com tipagem estática robusta, excelente desempenho e confiabilidade em sistemas corporativos.
* **Spring Boot:** Reduz o boilerplate de configuração, provê servidor Tomcat embutido e oferece integração nativa com o ecossistema REST (`Spring MVC` e `Jakarta Validation`).
* **Visual Studio Code:** Editor leve, com suporte a extensões como *Extension Pack for Java* e *Spring Boot Extension Pack*.

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
