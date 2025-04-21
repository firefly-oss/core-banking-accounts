# Core Banking Accounts Service

## Overview

The Core Banking Accounts Service is a microservice component of the Firefly platform that manages banking accounts and related operations. This service provides a comprehensive API for creating, updating, and managing bank accounts, account balances, account parameters, account providers, and account status history.

## Features

- **Account Management**: Create, read, update, and delete bank accounts
- **Balance Tracking**: Monitor and manage account balances
- **Parameter Management**: Configure and manage account parameters
- **Provider Integration**: Connect with external account providers
- **Status History**: Track account status changes over time
- **Reactive Architecture**: Built with Spring WebFlux for non-blocking operations

## Architecture

The service follows a modular architecture with clear separation of concerns:

### Module Structure

- **core-banking-accounts-interfaces**: Contains DTOs, interfaces, and enums that define the API contracts
- **core-banking-accounts-models**: Contains database entities and repositories
- **core-banking-accounts-core**: Contains business logic, services, and mappers
- **core-banking-accounts-web**: Contains REST controllers and application configuration

### Technology Stack

- **Java 21**: Utilizing the latest Java features including virtual threads
- **Spring Boot**: Application framework
- **Spring WebFlux**: Reactive web framework
- **R2DBC**: Reactive database connectivity
- **PostgreSQL**: Database for persistent storage
- **Flyway**: Database migration tool
- **Swagger/OpenAPI**: API documentation
- **Maven**: Build and dependency management

## Setup and Installation

### Prerequisites

- Java 21 or higher
- Maven 3.8 or higher
- PostgreSQL 14 or higher
- Docker (optional, for containerized deployment)

### Environment Variables

The following environment variables need to be set:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=accounts_db
DB_USERNAME=postgres
DB_PASSWORD=postgres
DB_SSL_MODE=disable
```

### Building the Application

```bash
mvn clean install
```

### Running the Application

```bash
# Run with Maven
mvn spring-boot:run -pl core-banking-accounts-web

# Run the JAR file
java -jar core-banking-accounts-web/target/core-banking-accounts-web-1.0.0-SNAPSHOT.jar

# Run with Docker
docker build -t core-banking-accounts .
docker run -p 8080:8080 --env-file .env core-banking-accounts
```

## API Documentation

The service provides a Swagger UI for exploring and testing the API. When running locally, access the Swagger UI at:

```
http://localhost:8080/swagger-ui.html
```

### API Endpoints

The service exposes the following main API endpoints:

- `/api/v1/accounts`: Account management endpoints
- `/api/v1/accounts/{accountId}/balances`: Account balance endpoints
- `/api/v1/account-parameters`: Account parameter endpoints
- `/api/v1/account-providers`: Account provider endpoints
- `/api/v1/account-status-history`: Account status history endpoints

## Development Guidelines

### Code Structure

- Follow the package structure based on domain concepts
- Keep controllers thin, with business logic in service classes
- Use DTOs for API contracts and entities for database models
- Use mappers to convert between DTOs and entities

### Testing

- Write unit tests for services and mappers
- Write integration tests for repositories
- Write API tests for controllers

### Database Migrations

Database migrations are managed with Flyway. Migration scripts are located in:

```
core-banking-accounts-models/src/main/resources/db/migration
```

To add a new migration, create a new SQL file with the naming convention `V{number}__{description}.sql`.

## Monitoring and Health Checks

The service exposes the following endpoints for monitoring:

- `/actuator/health`: Health check endpoint
- `/actuator/info`: Application information
- `/actuator/prometheus`: Metrics for Prometheus

## Configuration Profiles

The application supports multiple configuration profiles:

- **dev**: Development environment with detailed logging
- **testing**: Testing environment with API documentation enabled
- **prod**: Production environment with minimal logging and disabled API documentation