# Building Modular Monolith Applications with Spring Modulith

This repository showcases a Modular Monolith implementation of borrowing books in a library with 
Spring Boot, Spring Modulith and Domain Driven Design principles.

The code is explained in a series of blog posts.

1. [Building Modular Monolith Applications with Spring Boot and Domain Driven Design](https://itnext.io/building-modular-monolith-applications-with-spring-boot-and-domain-driven-design-d3299b300850) - First attempt at building a Modular Monolith code with only Spring Boot and DDD (does not use Spring Modulith).
2. TBA - Improve the implementation with Spring Modulith

## Project Requirements

* JDK 21
* Spring Boot 3.2

## Run the application

Run the application with the command: `mvn spring-boot:run`.

## Swagger REST API Docs
Access the Swagger UI at http://localhost:8080/swagger-ui.html

![image](https://github.com/xsreality/spring-modulith-with-ddd/assets/4991449/df13827e-dfe3-41a3-bec2-33f71288d3ad)

## Examples

### Add book to Library
```bash
curl -X POST -H Content-Type:application/json http://localhost:8080/books \
  -d '{"title":"Sapiens","inventoryNumber":"12345","isbn":"9428104","author":"Yuval Noah Harari"}' | jq
```

Response:
```json
{
  "id": 1,
  "title": "Sapiens",
  "inventoryNumber": {
    "barcode": "12345"
  },
  "isbn": "9428104",
  "author": {
    "name": "Yuval Noah Harari"
  },
  "status": "AVAILABLE"
}
```

### Checkout (borrow) a book

```bash
curl -X POST -H Content-Type:application/json http://localhost:8080/loans \
  -d '{"barcode": "12345"}' | jq
```

Response:
```json
{
  "id": 1,
  "bookBarcode": "12345",
  "patronId": null,
  "dateOfIssue": "2023-12-10",
  "loanDurationInDays": 14,
  "dateOfReturn": null,
  "status": "ACTIVE"
}
```

### Checkin (return) a book

```bash
curl -X DELETE http://localhost:8080/loans/1 | jq
```

Response:
```json
{
  "id": 1,
  "bookBarcode": "12345",
  "patronId": null,
  "dateOfIssue": "2023-12-10",
  "loanDurationInDays": 14,
  "dateOfReturn": "2023-12-10",
  "status": "COMPLETED"
}
```
