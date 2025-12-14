# Contributing to Budget Tracker API

Thank you for your interest in contributing to the Budget Tracker API! This document provides guidelines and instructions for contributing to this project.

## Table of Contents

- [Code of Conduct](#code-of-conduct)
- [Getting Started](#getting-started)
- [Development Workflow](#development-workflow)
- [Coding Standards](#coding-standards)
- [Testing Requirements](#testing-requirements)
- [Commit Messages](#commit-messages)
- [Pull Request Process](#pull-request-process)

## Code of Conduct

- Be respectful and inclusive
- Provide constructive feedback
- Focus on what is best for the community
- Show empathy towards other community members

## Getting Started

1. **Fork the repository** on GitHub
2. **Clone your fork** locally:
   ```bash
   git clone https://github.com/YOUR_USERNAME/BudgetTracker.git
   cd BudgetTracker
   ```
3. **Add upstream remote**:
   ```bash
   git remote add upstream https://github.com/dkothari21/BudgetTracker.git
   ```
4. **Create a branch** for your changes:
   ```bash
   git checkout -b feature/your-feature-name
   ```

## Development Workflow

1. **Keep your fork updated**:
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Make your changes** following the coding standards

3. **Run tests** to ensure nothing breaks:
   ```bash
   ./gradlew test
   ```

4. **Commit your changes** with clear commit messages

5. **Push to your fork**:
   ```bash
   git push origin feature/your-feature-name
   ```

6. **Create a Pull Request** on GitHub

## Coding Standards

### Java Code Style

- Follow standard Java naming conventions
- Use meaningful variable and method names
- Keep methods focused and concise (Single Responsibility Principle)
- Add JavaDoc comments for public APIs
- Use Lombok annotations to reduce boilerplate code

### Code Organization

- **Controllers**: Handle HTTP requests/responses only
- **Services**: Contain business logic
- **Repositories**: Handle data access
- **DTOs**: Transfer data between layers
- **Entities**: Represent database tables

### Example Code Structure

```java
@Service
@RequiredArgsConstructor
public class ExampleServiceImpl implements ExampleService {
    
    private final ExampleRepository repository;
    private final ExampleMapper mapper;
    
    @Override
    public ExampleDto.Response createExample(ExampleDto.Request request) {
        // Validation
        validateRequest(request);
        
        // Business logic
        Example entity = mapper.toEntity(request);
        Example saved = repository.save(entity);
        
        // Return response
        return mapper.toResponse(saved);
    }
    
    private void validateRequest(ExampleDto.Request request) {
        // Validation logic
    }
}
```

## Testing Requirements

### Unit Tests

- Write unit tests for all service methods
- Use Mockito for mocking dependencies
- Aim for at least 70% code coverage
- Test both success and failure scenarios

### Integration Tests

- Write integration tests for controllers
- Use `@SpringBootTest` and `MockMvc`
- Test complete request/response cycles
- Verify HTTP status codes and response bodies

### Test Naming Convention

```java
@Test
@DisplayName("Should create budget successfully")
void createBudget_Success() {
    // Arrange
    // Act
    // Assert
}

@Test
@DisplayName("Should throw exception when budget not found")
void createBudget_NotFound() {
    // Arrange
    // Act & Assert
}
```

### Running Tests

Before submitting a PR, ensure all tests pass:

```bash
# Run all tests
./gradlew test

# Run with coverage
./gradlew test jacocoTestReport

# Run specific test
./gradlew test --tests "com.example.budgetapp.service.impl.BudgetServiceImplTest"
```

## Commit Messages

Follow the conventional commits specification:

```
<type>(<scope>): <subject>

<body>

<footer>
```

### Types

- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `test`: Adding or updating tests
- `chore`: Maintenance tasks

### Examples

```
feat(budget): add monthly budget summary endpoint

Implemented a new endpoint to retrieve monthly budget summaries
including total budgeted amount and actual spending.

Closes #123
```

```
fix(auth): resolve JWT token expiration issue

Fixed issue where JWT tokens were expiring prematurely due to
incorrect time unit conversion.

Fixes #456
```

## Pull Request Process

1. **Update documentation** if you're adding new features
2. **Add tests** for new functionality
3. **Ensure all tests pass**:
   ```bash
   ./gradlew clean build
   ```
4. **Update the README.md** if needed
5. **Create a Pull Request** with a clear title and description
6. **Link related issues** in the PR description
7. **Wait for review** and address any feedback

### PR Title Format

```
[Type] Brief description of changes
```

Examples:
- `[Feature] Add transaction filtering by date range`
- `[Fix] Resolve budget calculation error`
- `[Docs] Update API documentation`

### PR Description Template

```markdown
## Description
Brief description of what this PR does

## Type of Change
- [ ] Bug fix
- [ ] New feature
- [ ] Breaking change
- [ ] Documentation update

## Testing
- [ ] All existing tests pass
- [ ] New tests added for new functionality
- [ ] Manual testing completed

## Checklist
- [ ] Code follows project style guidelines
- [ ] Self-review completed
- [ ] Comments added for complex code
- [ ] Documentation updated
- [ ] No new warnings generated

## Related Issues
Closes #issue_number
```

## Questions?

If you have questions or need help, please:
- Open an issue on GitHub
- Reach out to the maintainers

Thank you for contributing! 🎉
