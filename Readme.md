# Money Transfer

Money Transfer is a simple API providing money transfers 
between accounts.

## Technologies

Here's our technology stack.

Language: Java 8

Build & Dependency Tool: Maven

Preferred IDE: Intellij IDEA

Web-service: 
 - JAX-RS
 - Jersey 2
 - Grizzly 
 - Jackson
  
Persistence:
 - JPA
 - Hibernate
 - H2 in-memory db
 
Unit & Integration Testing: 
 - JUnit 4  

## Build

Pre-requisites:
 - Java 1.8
 - Maven

To build the project run command:

```
mvn clean package
```

This also runs all tests.

At the end this will create `target/moneytransfer-1.0-SNAPSHOT-jar-with-dependencies.jar`.

## Run 

To run the server:

```
java -jar target/moneytransfer-1.0-SNAPSHOT-jar-with-dependencies.jar 
```

Hit Enter to shutdown the server.

## API Usage

Sample examples below use cURL to run API requests.

There is also [Requests Playbook](playbook.http) which can be 
used to run sample requests using IDE like Intellij IDEA Ultimate.

### Create new account

```
curl -XPOST -H "Content-Type: application/json" \
    -d '{"amount": 1000.00, "currency": "EUR"}' \
    http://localhost:8080/moneytransfer/accounts
```

responds with account details

```json
{
 "accountNo":1,
 "amount":1000.0,
 "currency":"EUR"
}
```

### Get account details

Request: 
```
curl -XGET http://localhost:8080/moneytransfer/accounts/1
```

Response:
```json
{
 "accountNo":1,
 "amount":1000.0,
 "currency":"EUR"
}
```

### Transfer money

Transfer money from account №1 to account №2 in amount of 150.00.

```
curl -XPOST -H "Content-Type: application/json" \
    -d '{"fromAccount": 1, "amount": 150.00, "currency": "EUR", "toAccount": 2}' \
    http://localhost:8080/moneytransfer/transfer
```

Response:

```json
{
 "fromAccount":1,
 "amount":150.0,
 "currency":"EUR",
 "toAccount":2,
 "completed":true
}
```

### Account transactions

```
curl -XGET http://localhost:8080/moneytransfer/accounts/2/transactions
```

Response:

```json
[
 {
  "id":2,
  "amount":150.00,
  "currency":"EUR"
 }
]
```

