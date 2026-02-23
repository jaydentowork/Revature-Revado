# Revado - Todo Application

A RESTful Todo application built with Spring Boot and secured with JWT authentication.

## Tech Stack

- **Java** - Core language
- **Spring Boot** - Application framework
- **Spring Security** - Authentication & authorization
- **JWT** - Stateless token-based authentication
- **Maven** - Dependency management

## Features

- User registration and login
- JWT-based authentication
- Create, read, update, and delete todos
- Filter todos by completion status
- Create subtasks for todo manually or with LLM
  - Ingrate openrouter for LLM generation, define api key and model type in application.properties
- Swagger UI for viewing schema and endpoints: http://localhost:8080/swagger-ui/index.html#/
## Getting Started

### Prerequisites

- Java 17+
- Maven 3.6+

### Running the Application

1. Clone the repository:
   ```bash
   git clone https://github.com/jaydentowork/Revature-Revado.git

2. Setup the application.properties based on the application-example.properties file
