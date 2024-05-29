# Building Modular Monolith Applications with Spring Modulith and Domain Driven Design

This repository showcases a Modular Monolith implementation of borrowing books in a library with 
Spring Boot, Spring Modulith and Domain Driven Design principles.

The code is explained in a series of blog posts.

1. [Building Modular Monolith Applications with Spring Boot and Domain Driven Design](https://itnext.io/building-modular-monolith-applications-with-spring-boot-and-domain-driven-design-d3299b300850?sk=3c3179d82508b50cc490a2a47074804f) - First attempt at building a Modular Monolith code with only Spring Boot and DDD (does not use Spring Modulith). The code is available in branch [part-1-ddd-solution](https://github.com/xsreality/spring-modulith-with-ddd/tree/part-1-ddd-solution).
2. [Improving Modular Monolith Applications with Spring Modulith](https://itnext.io/improving-modular-monolith-applications-with-spring-modulith-edecc787f63c?sk=051ea353e17154843886705fb90ed64a) - In this blog, we rethink the domain model and apply eventual consistency with Spring Modulith to make the application easier to test, self-documenting and more maintainable. The code is available in branch [part-2-spring-modulith](https://github.com/xsreality/spring-modulith-with-ddd/tree/part-2-spring-modulith).
3. [Adopting Domain-First Thinking in Modular Monolith with Hexagonal Architecture](https://itnext.io/adopting-domain-first-thinking-in-modular-monolith-with-hexagonal-architecture-f9e4921ac18d?sk=9364f2aac410c7b72e75e189bfa240e9) - In this blog, we re-implement the Borrow module with Hexagonal instead of Layered architecture. We demonstrate how absolutely no changes were needed in the Catalog module even though they are part of the same monolith code base. The code is available in branch [part-3-hexagonal-architecture](https://github.com/xsreality/spring-modulith-with-ddd/tree/part-3-hexagonal-architecture) and `main`.
4. [Securing Modular Monolith with OAuth2 and Spring Security](https://itnext.io/securing-modular-monolith-with-oauth2-and-spring-security-43f2504c4e2e?sk=d70b9e7b343a2d0b690272d3b153dae3) - In this blog, we add OAuth2 authentication and authorization with Spring Security. Using Spring Modulith _shared_ module concept, we create a shared security module. The code is available in branch [part-4-authentication](https://github.com/xsreality/spring-modulith-with-ddd/tree/part-4-authentication) and `main`.

## Project Requirements

* JDK 21
* Spring Boot 3.3

## The Business Problem

1. The library consists of thousands of books. There can be multiple copies of the same book.
2. Before being included in the library, every book receives a barcode stamped at the back or one of the end pages. This barcode number uniquely identifies the book.
3. A patron of the library can make a request to place a book on hold by either locating the book in the library or directly going to the circulation desk and ask for a book by title. If book is available, the patron can proceed to checkout (collect) the book. 
4. A patron cannot check out a book held by a different patron.
5. The book is checked out for a fixed period of 2 weeks.
6. To check in (return) the book, the patron can go to the circulation desk or drop it in the drop zone.
7. Only staff members (users with role `ROLE_STAFF`) can add a book to the catalog.

## Bounded Contexts

![image](https://github.com/xsreality/spring-modulith-with-ddd/assets/4991449/2f8947e9-2630-411a-a14b-099f4bcfed89)

## Prepare the application

To compile and build the docker images, run below command:

```bash
mvn spring-boot:build-image
```

This will generate a docker image locally - `spring-modulith-with-ddd:0.0.1-SNAPSHOT`.

## Run the application

The project comes with a docker compose file which spins up the application as well as Keycloak, the Authorization server for OAuth2 flow. After completing the steps in "Prepare the application", run below command to start the application:

```bash
docker-compose up
```

## Authentication

The project uses OAuth2 flows for authentication implemented with Spring Security. The Authorization server is Keycloak (installed automatically via docker compose). The OIDC discovery document can be accessed at http://localhost:8083/realms/library/.well-known/openid-configuration.

Keycloak is preconfigured with a realm named `library`. It has 2 users - `john` and `winston` with the credentials `password`. A public client with client ID `library` is also configured to trigger the Authorization code flow.

An access token can be obtained by using any client like Postman or Insomnia to trigger the Authorization code flow. 

## Swagger REST API Docs
Access the Swagger UI at http://localhost:8080/swagger-ui/index.html

![image](https://github.com/xsreality/spring-modulith-with-ddd/assets/4991449/fcfb3e49-3024-4850-ba6e-dfeb9211caff)

## Examples

### Add book to Library
```bash
curl -X POST -H Content-Type:application/json http://localhost:8080/catalog/books \
  -d '{"title":"Sapiens","catalogNumber":"12345","isbn":"9780062316097","author":"Yuval Noah Harari"}' | jq
```

Response:
```json
{
  "id": 1,
  "title": "Sapiens",
  "catalogNumber": {
    "barcode": "12345"
  },
  "isbn": "9780062316097",
  "author": {
    "name": "Yuval Noah Harari"
  }
}
```

### Place a book on hold (start the borrowing process)

```bash
curl -X POST -H Content-Type:application/json http://localhost:8080/borrow/holds \
  -d '{"barcode": "12345", "patronId": "018dd2f7-b241-7d27-be99-45fb3f145ddf"}' | jq
```

Response:
```json
{
  "id": "8c8702af-9363-4953-94a5-2ddfa5aea631",
  "bookBarcode": "12345",
  "patronId": "018dd2f7-b241-7d27-be99-45fb3f145ddf",
  "dateOfHold": "2024-02-22"
}
```
