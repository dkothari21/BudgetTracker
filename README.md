# Personal Budget Tracker API

A Spring Boot REST API for managing personal budgets, built with Java 17 and Spring Boot 3.4.0.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen)
![Gradle](https://img.shields.io/badge/Gradle-8.5-blue)
![License](https://img.shields.io/badge/License-MIT-yellow)

## Table of Contents

- [Prerequisites](#prerequisites)
- [Running the API in Development Mode](#running-the-api-in-development-mode)
- [Configuration](#configuration)
- [API Endpoints](#api-endpoints)
- [Testing](#testing)
- [Code Coverage](#code-coverage)
- [Project Structure](#project-structure)
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

> [!WARNING]
> **JWT Secret Configuration**
> 
> The JWT secret is currently hardcoded in `application.yml` for development convenience. 
> 
> **For production deployments**, you MUST:
> 1. Remove the hardcoded secret from `application.yml`
> 2. Set the JWT secret as an environment variable:
>    ```bash
>    export JWT_SECRET=your-secure-random-secret-key-here
>    ```
> 3. Update `application.yml` to reference the environment variable:
>    ```yaml
>    app:
>      jwt:
>        secret: ${JWT_SECRET}
>    ```
> 
> Generate a secure secret using:
> ```bash
> openssl rand -base64 64
> ```

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

## API Documentation with Swagger UI

The API includes interactive Swagger UI documentation for easy testing and exploration.

### Accessing Swagger UI

Once the application is running, open your browser and navigate to:

**http://localhost:8080/swagger-ui/index.html**

### How to Test with Swagger UI

> 📖 **New to Swagger?** See the complete beginner's guide: [SWAGGER_GUIDE.md](SWAGGER_GUIDE.md)  
> 🔑 **How to add token?** See the visual guide: [HOW_TO_ADD_TOKEN.md](HOW_TO_ADD_TOKEN.md)

**Quick Start:**

1. **Start the Application**
   ```bash
   ./gradlew bootRun
   ```

2. **Open Swagger UI** in your browser at http://localhost:8080/swagger-ui/index.html

3. **Register a New User**
   - Expand the `auth-controller` section
   - Click on `POST /api/auth/register`
   - Click "Try it out"
   - Enter user details and click "Execute"

4. **Login to Get JWT Token**
   - Click on `POST /api/auth/login`
   - Enter your credentials and click "Execute"
   - **Copy the token** from the response

5. **Authorize Swagger UI**
   - Click the **"Authorize"** button (🔒) at the top
   - Paste your JWT token in the "Value" field
   - Click "Authorize" then "Close"

6. **Test Protected Endpoints**
   - Create a category → Create a budget → Create an account → Create a transaction
   - All requests will automatically include your JWT token

**Complete Testing Flow:**
```
Register → Login → Authorize → Create Category → Create Budget → Create Account → Create Transaction
```

For detailed step-by-step instructions with screenshots and troubleshooting, see [SWAGGER_GUIDE.md](SWAGGER_GUIDE.md).

### Alternative: OpenAPI JSON

Access the raw OpenAPI specification at:
- **http://localhost:8080/v3/api-docs**

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

The project includes comprehensive unit and integration tests with code coverage reporting.

### Test Structure

```
src/test/java/
├── BudgetAppApplicationTests.java          # Application context test
├── controller/
│   └── AuthControllerTest.java             # Auth endpoint integration tests
└── service/impl/
    ├── AuthServiceImplTest.java            # Auth service unit tests
    └── BudgetServiceImplTest.java          # Budget service unit tests
```

### Run All Tests

```bash
./gradlew test
```

### Run Tests with Coverage Report

```bash
./gradlew test jacocoTestReport
```

The HTML coverage report will be generated at:
```
build/reports/jacoco/test/html/index.html
```

### Run Specific Test Class

```bash
./gradlew test --tests "com.example.budgetapp.service.impl.BudgetServiceImplTest"
```

### Run Tests in Continuous Mode

```bash
./gradlew test --continuous
```

## Code Coverage

The project uses JaCoCo for code coverage analysis. Coverage reports exclude:
- Configuration classes
- DTOs (Data Transfer Objects)
- Entity classes
- Main application class

**Coverage Target**: 70% minimum

To view the coverage report after running tests:
```bash
open build/reports/jacoco/test/html/index.html
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
budgettracker/
├── gradle/
│   └── wrapper/                 # Gradle wrapper files
├── src/
│   ├── main/
│   │   ├── java/com/example/budgetapp/
│   │   │   ├── config/          # Security & app configuration
│   │   │   ├── controller/      # REST controllers
│   │   │   ├── dto/             # Data Transfer Objects
│   │   │   ├── entity/          # JPA entities
│   │   │   ├── exception/       # Custom exceptions
│   │   │   ├── mapper/          # MapStruct mappers
│   │   │   ├── repository/      # Data repositories
│   │   │   ├── security/        # JWT & security components
│   │   │   ├── service/         # Service interfaces
│   │   │   │   └── impl/        # Service implementations
│   │   │   └── BudgetAppApplication.java
│   │   └── resources/
│   │       └── application.yml  # Application configuration
│   └── test/
│       ├── java/com/example/budgetapp/
│       │   ├── controller/      # Controller integration tests
│       │   └── service/impl/    # Service unit tests
│       └── resources/
│           └── application-test.yml  # Test configuration
├── build.gradle                 # Gradle build configuration
├── gradlew                      # Gradle wrapper script (Unix/Mac)
├── gradlew.bat                  # Gradle wrapper script (Windows)
├── docker-compose.yml           # PostgreSQL setup
├── test-endpoints.sh            # Automated API testing script
├── SWAGGER_GUIDE.md             # Complete Swagger UI testing guide
├── CONTRIBUTING.md              # Contribution guidelines
└── README.md                    # This file
```

## Support

For issues or questions, please check the application logs:

```bash
./gradlew bootRun --debug
```

---

**Happy Coding! 🚀**
