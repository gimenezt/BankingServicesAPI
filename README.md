# BankingServicesAPI
A **Banking Services API** é um serviço desenvolvido em Java 17 com Spring Boot, destinado a simular operações bancárias simples, como o cadastro de clientes e a transferência entre contas.

## Tecnologias Utilizadas
- Java 17
- Spring Boot
- H2 (Banco de dados em memória)
- JPA (Java Persistence API)
- Maven (Gerenciador de dependências)
- GitHub (Controle de versão)

## Pré-requisitos
Antes de rodar a aplicação localmente, é necessário ter o Java 17 instalado no seu computador. Você pode verificar se o Java está instalado corretamente com o comando no seu CMD:
~~~bash
java -version
~~~
Para baixar, acesse: https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html

## Rodando a aplicação
1. Clone o repositório:
    ~~~bash
    git clone https://github.com/gimenezt/BankingServicesAPI.git
    cd BankingServicesAPI
    ~~~
2. Instale as dependências com o Maven:
   ~~~bash
    mvn clean install
    ~~~
3. Execute a aplicação:
    ~~~bash
    mvn spring-boot:run
    ~~~

## Testando a API
Para rodar os testes automatizados da API, utilize:
~~~bash
mvn test
~~~

## Endpoints da API
A porta utilizada para a API é **8080**. Portanto, quando for utilizar os endpoimts, utilize: `http://localhost:8080/`.

### 1. `POST /client` - Cadastro um cliente
- Requisição:
    ~~~json
    {
        "name": "Nome do Cliente",
        "accountNumber": "12345",
        "balance": 1000.00
    }
    ~~~
- Resposta:
    - Status 201 - Created
    ~~~json
    {
        "id":1,
        "name":"Nome do Cliente",
        "accountNumber":"12345",
        "balance":	1000.00
    }
    ~~~
    - Status 400 - Bad Request (com a mensagem explicando qual o erro)
### 2. `GET /client` - Lista todos os clientes cadastrados
- Resposta:
    - Status 200 - OK
    ~~~json
    [
        {
            "id":1,
            "name":"Nome do Cliente",
            "accountNumber":"12345",
            "balance":	1000.00
        },
        {
            "id":2,
            "name":"Nome do Cliente2",
            "accountNumber":"22222",
            "balance":	1000.00
        }
    ]
    ~~~
### 3. `GET /client/{accountNumber}` - Busca um cliente pelo número da conta
- Requisição: (URL) `/client/12345`
- Resposta:
    - Status 200 - OK
    ~~~json
    {
        "name": "Nome do Cliente",
        "accountNumber": "12345",
        "balance": 1000.00
    }
    ~~~
    - Status 404 - Not Found (com a mensagem explicando qual o erro)
### 4. `GET /transaction` - Lista todas as transações, incluindo falhas
- Resposta:
    - Status 200 - OK
    ~~~json
    [
        {
            "id": 1,
            "accountOrigin": "12345",
            "accountDestination": "22222",
            "transactionStatus": "FAILED",
            "amount": 150,
            "datetime": "2025-04-13T18:36:26.805582"
        },
        {
            "id": 2,
            "accountOrigin": "12345",
            "accountDestination": "22222",
            "transactionStatus": "SUCCESS",
            "amount": 100,
            "datetime": "2025-04-13T18:40:26.805582"
        }
    ]
    ~~~
### 5. `GET /transaction/{accountOrigin}` - Lista de transações realizadas por uma conta, incluindo falhas
- Requisição: (URL) `/transaction/12345`
- Resposta:
    - Status 200 - OK
    ~~~json
    [
        {
            "id": 1,
            "accountOrigin": "12345",
            "accountDestination": "22222",
            "transactionStatus": "FAILED",
            "amount": 150,
            "datetime": "2025-04-13T18:36:26.805582"
        },
        {
            "id": 2,
            "accountOrigin": "12345",
            "accountDestination": "22222",
            "transactionStatus": "SUCCESS",
            "amount": 100,
            "datetime": "2025-04-13T18:40:26.805582"
        }
    ]
    ~~~
    - Status 404 - Not Found (com a mensagem explicando qual o erro)
### 6. `POST /transaction` - Processa uma nova transação
- Requisição:
    ~~~json
    {
        "accountOrigin": "12345",
        "accountDestination": "67890",
        "amount": 100
    }
    ~~~
- Resposta:
    - Status 200 - OK
    ~~~json
    {
        "id": 1,
        "accountOrigin": "12345",
        "accountDestination": "22222",
        "transactionStatus": "SUCCESS",
        "amount": 100,
        "datetime": "2025-04-13T18:36:26.805582"
    }
    ~~~
    - Status 400 - Bad Request (com a mensagem explicando qual o erro)
## Fluxo Completo de Uso (Exemplificação)
1. Cadastrar dois clientes, um por vez, com `POST /client`:
    ~~~json
    // Cliente 1
    {
        "name": "Maurício Silva",
        "accountNumber": "12345",
        "balance": 1000
    }
    // Cliente 2
    {
        "name": "Sônia Segura",
        "accountNumber": "12348",
        "balance": 1200
    }
    ~~~
2. Realizar transferência com `POST /transaction` (0 < valor da transferência <= 100):
    ~~~json
    {
        "accountOrigin": "12345",
        "accountDestination": "12348",
        "amount": 100
    }
    ~~~
3. Verificar saldos atualizados com `GET /client` (para ver todas as informações todos clientes registrados) ou com `GET /client/{accountNumber}` (para ver apenas as informações de uma conta em específico).
4. E para visualizar histórico de transações, utilize `GET /transaction` (para ver todas as transações processadas) ou `GET /transaction/{accountOrigin}` (para ver apenas as transações de uma conta em específico).