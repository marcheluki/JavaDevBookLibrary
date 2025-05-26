# Library Management System API

This is the Spring Boot API microservice for the full-stack Library Management System.
It provides the backend functionality for managing book data.

## Project Structure

```
src/main/java/com/library/api/
├── controller/     # REST controllers
├── service/        # Business logic
├── repository/     # Data access layer
├── model/          # Entity classes
└── exception/      # Custom exceptions
```

## Setup

To run the API as part of the full system, use Docker Compose from the project root directory (see main README.md).

You can also run the API independently using Maven:

1. Make sure you have Java 17 or later installed
2. Make sure you have Maven installed
3. Navigate to the `api` directory:
   ```bash
   cd api
   ```
4. Run the application:
   ```bash
   mvn spring-boot:run
   ```

## API Endpoints

The API endpoints are available under the `/api` path. When running with Docker Compose, the API is accessible via the frontend's Nginx proxy at `http://localhost/api`. Direct access to the API service within the Docker network is typically on port 8080.

## Database

When running with Docker Compose, the application uses a PostgreSQL database for data persistence.

If running the API independently with `mvn spring-boot:run`, it might default to an H2 in-memory database based on the original configuration (check `application.properties` or `application.yml` in `src/main/resources`). The H2 console might be available at `http://localhost:8080/h2-console` with the following credentials:

- JDBC URL: `jdbc:h2:mem:librarydb`
- Username: `sa`
- Password: `password` 