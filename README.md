# Building Modular Monolith Applications with Spring Modulith and Domain Driven Design

This repository showcases a Modular Monolith implementation of borrowing books in a library with 
Spring Boot, Spring Modulith and Domain Driven Design principles.

The code is explained in a series of blog posts.

1. [Building Modular Monolith Applications with Spring Boot and Domain Driven Design](https://itnext.io/building-modular-monolith-applications-with-spring-boot-and-domain-driven-design-d3299b300850) - First attempt at building a Modular Monolith code with only Spring Boot and DDD (does not use Spring Modulith). The code is available in branch [part-1-ddd-solution](https://github.com/xsreality/spring-modulith-with-ddd/tree/part-1-ddd-solution).
2. [Improving Modular Monolith Applications with Spring Modulith](https://itnext.io/improving-modular-monolith-applications-with-spring-modulith-edecc787f63c) - In this blog, we rethink the domain model and apply eventual consistency with Spring Modulith to make the application easier to test, self-documenting and more maintainable. The code is available in branch [part-2-spring-modulith](https://github.com/xsreality/spring-modulith-with-ddd/tree/part-2-spring-modulith) and `main`.
3. TBA - Further improvements with Hexagonal architecture.

## Project Requirements

* JDK 21
* Spring Boot 3.2

## Run the application

Run the application with the command: `mvn spring-boot:run`.

## Swagger REST API Docs
Access the Swagger UI at http://localhost:8080/swagger-ui.html

![image](https://github.com/xsreality/spring-modulith-with-ddd/assets/4991449/c1cfedf5-97cd-4c22-948c-a6ba999ae4f4)

## Examples

### Add book to Library
```bash
curl -X POST -H Content-Type:application/json http://localhost:8080/catalog/books \
  -d '{"title":"Sapiens","inventoryNumber":"12345","isbn":"9428104","author":"Yuval Noah Harari"}' | jq
```

Response:
```json
{
  "id": 1,
  "title": "Sapiens",
  "catalogNumber": {
    "barcode": "12345"
  },
  "isbn": "9428104",
  "author": {
    "name": "Yuval Noah Harari"
  }
}
```

### Place a book on hold (start the borrowing process)

```bash
curl -X POST -H Content-Type:application/json http://localhost:8080/borrow/loans \
  -d '{"barcode": "12345"}' | jq
```

Response:
```json
{
  "id": 1,
  "bookId": 1,
  "patronId": null,
  "dateOfHold": "2023-12-28",
  "dateOfCheckout": null,
  "holdDurationInDays": 3,
  "loanDurationInDays": 0,
  "dateOfCheckin": null,
  "status": "HOLDING"
}
```


### Checkout (collect) a book

```bash
curl -X POST http://localhost:8080/borrow/loans/1/checkout | jq
```

Response:
```json
{
  "id": 1,
  "bookId": 1,
  "patronId": null,
  "dateOfHold": "2023-12-28",
  "dateOfCheckout": "2023-12-28",
  "holdDurationInDays": 3,
  "loanDurationInDays": 14,
  "dateOfCheckin": null,
  "status": "ACTIVE"
}
```

### Checkin (return) a book

```bash
curl -X POST http://localhost:8080/borrow/loans/1/checkin | jq
```

Response:
```json
{
  "id": 1,
  "bookId": 1,
  "patronId": null,
  "dateOfHold": "2023-12-28",
  "dateOfCheckout": "2023-12-28",
  "holdDurationInDays": 3,
  "loanDurationInDays": 14,
  "dateOfCheckin": "2023-12-28",
  "status": "COMPLETED"
}
```
