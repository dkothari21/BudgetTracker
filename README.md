# Personal Budget Tracker API

A Spring Boot REST API for managing personal budgets, built with Java 17 and Spring Boot 3.4.0.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the API in Development Mode](#running-the-api-in-development-mode)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Troubleshooting](#troubleshooting)

## Prerequisites

Before running the API, ensure you have the following installed:

- **Java 17** or higher ([Download](https://adoptium.net/))
- **Gradle** (optional - the project includes Gradle Wrapper)
- **Docker** (optional - only if using PostgreSQL instead of H2)

Verify your Java installation:
```bash
java -version
```

## Running the API in Development Mode

### Option 1: Using Gradle Wrapper (Recommended)

The easiest way to run the API on **localhost:8080** in development mode:

```bash
./gradlew bootRun
```

On Windows:
```bash
gradlew.bat bootRun
```

The API will start on **http://localhost:8080** by default.

### Option 2: Using Gradle with Custom Port

To run on a specific port (e.g., 8080):

```bash
./gradlew bootRun --args='--server.port=8080'
```

### Option 3: Build and Run JAR

1. Build the project:
```bash
./gradlew build
```

2. Run the JAR file:
```bash
java -jar build/libs/budgetapp-0.0.1-SNAPSHOT.jar --server.port=8080
```

### Option 4: Using Your IDE

#### IntelliJ IDEA
1. Open the project in IntelliJ IDEA
2. Navigate to `src/main/java/com/example/budgetapp/BudgetAppApplication.java`
3. Right-click on the file and select **Run 'BudgetAppApplication'**
4. The API will start on port 8080

#### VS Code
1. Open the project in VS Code
2. Install the "Spring Boot Extension Pack" if not already installed
3. Open the Spring Boot Dashboard
4. Click the play button next to the application
5. The API will start on port 8080

## Configuration

### Development Configuration

The API uses **H2 in-memory database** by default for development, which requires no additional setup.

Current configuration (`src/main/resources/application.yml`):

```yaml
spring:
  application:
    name: budgetapp
  datasource:
    url: jdbc:h2:mem:budgetdb
    username: sa
    password: password
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: update
    show-sql: true
```

### Accessing H2 Console

When running in development mode, you can access the H2 database console at:

**http://localhost:8080/h2-console**

Connection settings:
- **JDBC URL**: `jdbc:h2:mem:budgetdb`
- **Username**: `sa`
- **Password**: `password`

### Using PostgreSQL (Optional)

If you prefer to use PostgreSQL instead of H2:

1. Start PostgreSQL using Docker Compose:
```bash
docker-compose up -d
```

2. Update `application.yml` to use PostgreSQL:
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/budgetdb
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    database-platform: org.hibernate.dialect.PostgreSQLDialect
```

3. Run the application as usual

## API Endpoints

Once the API is running on **http://localhost:8080**, you can access the following:

### Health Check
```bash
curl http://localhost:8080/actuator/health
```

### Authentication Endpoints
- `POST /api/auth/register` - Register a new user
- `POST /api/auth/login` - Login and get JWT token

### Budget Endpoints
(Requires authentication - include JWT token in Authorization header)
- `GET /api/budgets` - Get all budgets
- `POST /api/budgets` - Create a new budget
- `GET /api/budgets/{id}` - Get budget by ID
- `PUT /api/budgets/{id}` - Update budget
- `DELETE /api/budgets/{id}` - Delete budget

### Example API Call

```bash
# Register a new user
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "email": "test@example.com",
    "password": "password123"
  }'

# Login
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "password": "password123"
  }'
```

## Testing

### Run All Tests
```bash
./gradlew test
```

### Run Tests with Coverage
```bash
./gradlew test jacocoTestReport
```

### Run Specific Test Class
```bash
./gradlew test --tests "com.example.budgetapp.YourTestClass"
```

## Troubleshooting

### Port Already in Use

If port 8080 is already in use, you'll see an error like:
```
Web server failed to start. Port 8080 was already in use.
```

**Solutions:**

1. **Use a different port:**
```bash
./gradlew bootRun --args='--server.port=8081'
```

2. **Find and kill the process using port 8080:**
```bash
# On macOS/Linux
lsof -ti:8080 | xargs kill -9

# On Windows
netstat -ano | findstr :8080
taskkill /PID <PID> /F
```

### Build Failures

If you encounter build issues:

1. **Clean the build:**
```bash
./gradlew clean build
```

2. **Clear Gradle cache:**
```bash
rm -rf ~/.gradle/caches/
./gradlew build --refresh-dependencies
```

### Database Connection Issues

If using PostgreSQL and encountering connection issues:

1. **Verify Docker container is running:**
```bash
docker ps
```

2. **Check PostgreSQL logs:**
```bash
docker logs budget-db
```

3. **Restart the database:**
```bash
docker-compose down
docker-compose up -d
```

### Application Won't Start

1. **Check Java version:**
```bash
java -version
```
Ensure you're using Java 17 or higher.

2. **Check for compilation errors:**
```bash
./gradlew compileJava
```

3. **View detailed logs:**
```bash
./gradlew bootRun --info
```

## Development Tips

### Hot Reload with Spring Boot DevTools

Add Spring Boot DevTools to your `build.gradle` for automatic restarts during development:

```gradle
dependencies {
    developmentOnly 'org.springframework.boot:spring-boot-devtools'
}
```

### Enable Debug Logging

Add to `application.yml`:
```yaml
logging:
  level:
    root: DEBUG
```

### API Documentation

Consider adding SpringDoc OpenAPI for automatic API documentation:

```gradle
implementation 'org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0'
```

Then access Swagger UI at: **http://localhost:8080/swagger-ui.html**

## Project Structure

```
personal-budget-tracker/
├── src/
│   ├── main/
│   │   ├── java/com/example/budgetapp/
│   │   │   ├── config/          # Security & app configuration
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── service/         # Business logic
│   │   │   └── BudgetAppApplication.java
│   │   └── resources/
│   │       └── application.yml  # Application configuration
│   └── test/                    # Test files
├── build.gradle                 # Gradle build configuration
├── docker-compose.yml           # PostgreSQL setup
└── README.md                    # This file
```

## Support

For issues or questions, please check the application logs:

```bash
./gradlew bootRun --debug
```

---

**Happy Coding! 🚀**
