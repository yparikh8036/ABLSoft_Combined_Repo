# Backend - Invoice CSV Upload API

A Spring Boot (Java 17, Gradle) REST API for uploading, parsing, and storing invoice data from CSV files. It includes endpoints for uploading CSV via multipart/form-data, creating/updating single invoice records, and retrieving stored invoices. The project is configured with OpenAPI/Swagger UI, CORS, and stateless security (all endpoints permitted by default).

## Features
- Upload CSV file with invoices (header row supported) via multipart form
- Create or update single invoice entries via JSON
- List all uploaded invoices and fetch by ID
- Duplicate prevention by customerId + invoiceNum
- OpenAPI documentation via Swagger UI

## Tech Stack
- Java 17
- Spring Boot 3.5.x (Web, Security, Data JPA)
- MapStruct for DTO mapping
- Apache Commons CSV for CSV parsing
- springdoc-openapi for API docs
- Gradle build system
- MySQL connector (runtime)

## Project Structure
- src/main/java/com/example/backend
  - AblsoftApplication.java — Spring Boot entry point
  - config — Security, CORS, and Swagger configuration
  - rest — REST controllers (UploadFileResource)
  - service — Business logic, CSV parsing (UploadFileService)
  - service/dto — DTOs (UploadFileDTO)
  - service/mapper — MapStruct mappers
  - domain — UploadFile entity
  - repository — Spring Data JPA repository (UploadFileRepo)
- src/main/resources
  - application.properties — application configuration
  - static/invoices.csv — sample CSV

## Getting Started

### Prerequisites
- Java 17 installed (JAVA_HOME set)

### Build
- Windows PowerShell (from project root):
  - .\gradlew.bat clean build
- Unix-like shells:
  - ./gradlew clean build

### Run
- Windows PowerShell:
  - .\gradlew.bat bootRun
- Unix-like shells:
  - ./gradlew bootRun

By default, the app starts on http://localhost:8080.

## Configuration
Edit src/main/resources/application.properties as needed. Typical properties include server port, datasource, JPA, etc. If using MySQL, ensure the proper spring.datasource.* and spring.jpa.* properties are provided.

## API Overview
Base path: /api

- POST /api/upload-file
  - Create a single invoice record from JSON body (UploadFileDTO). Fails if an ID is present or duplicate invoice exists.
- PUT /api/upload-file
  - Update an existing invoice record (UploadFileDTO with id required).
- GET /api/files
  - List all uploaded invoices.
- GET /api/files/{fileId}
  - Retrieve a single invoice by its ID.
- POST /api/upload (multipart/form-data)
  - Upload a CSV file with header row. Field name: file. Returns saved invoices excluding duplicates.

### CSV Format
CSV must be comma-separated and include a header row with columns:
- customerId
- invoiceNum
- date
- description
- amount

Example: see src/main/resources/static/invoices.csv

Notes:
- Duplicates (same customerId + invoiceNum) are filtered out during bulk upload.
- For single creates, duplicates trigger an error with BAD_REQUEST.

## Swagger UI
With the application running, open:
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

## Security & CORS
- Security is configured as stateless with CSRF disabled and all requests permitted by default (SecurityConfig).
- CORS is enabled with defaults (CorsConfig).

## Testing
Run tests:
- Windows PowerShell: .\gradlew.bat test
- Unix-like: ./gradlew test

## Docker
Quick start:
- Build image: docker build -t backend:latest .
- Run: docker run --rm -p 8080:8080 backend:latest

## Docker Compose
Quick start:
- Up: docker compose up --build
- Down: docker compose down

Exposes http://localhost:8080 by default.

### Docker Compose with MySQL
This repository includes a MySQL service in docker-compose.yml. To run the API with MySQL locally:
- Start services: docker compose up --build
- The app will connect to the db service using the following defaults:
  - SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/backend
  - SPRING_DATASOURCE_USERNAME=app
  - SPRING_DATASOURCE_PASSWORD=app
- MySQL credentials:
  - Database: backend
  - User/Password: app/app
  - Root password: root
- Persistent data volume: db_data (mounted at /var/lib/mysql)

If you already have MySQL running locally and don’t want the bundled db, you can comment out the db service and set SPRING_DATASOURCE_URL to point to your host DB (e.g., jdbc:mysql://host.docker.internal:3306/yourdb) in docker-compose.yml or via environment variables.

## Error Handling
- Custom error messages and codes are defined in service/errors (ErrorConstants, GlobalException) and surfaced through rest/errors.

