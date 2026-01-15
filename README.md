# JWT Authentication Application

A Spring Boot application template for JWT-based authentication and user management with email verification support.

## Project Overview

This project provides a complete authentication system using JSON Web Tokens (JWT) with Spring Security, including features like user registration, email verification, and secure API endpoints.

## Technology Stack

### Core Framework
- **Spring Boot**: 4.0.1
- **Java**: 21
- **Gradle**: 9.2.1

### Important Compatibility Notes ⚠️

**Version Alignment**: Ensure your IDE (IntelliJ IDEA) uses the same Gradle version as the wrapper. When Gradle versions don't match between your IDE and the wrapper configuration, it can cause build errors.

#### Recommended IDE Versions
- **IntelliJ IDEA**: 2024.1 or later (recommended for Java 21 and Gradle 9.2.1 support)
- **Gradle**: 9.2.1 (managed by gradle wrapper)

### Key Dependencies
- **Spring Boot Starters**:
  - `spring-boot-starter-data-jpa` - JPA and Hibernate ORM
  - `spring-boot-starter-security` - Spring Security
  - `spring-boot-starter-webmvc` - Spring MVC for REST APIs
  - `spring-boot-starter-mail` - Email support

- **Database**:
  - PostgreSQL driver

- **JWT**:
  - `jjwt-api:0.11.5` - JWT library
  - `jjwt-impl:0.11.5` - JWT implementation
  - `jjwt-jackson:0.11.5` - JSON serialization for JWT

- **Utilities**:
  - Lombok - Annotation processing for boilerplate code

- **Testing**:
  - JUnit Platform
  - Spring Boot test starters (JPA, Mail, Security, WebMvc)

## Project Structure

```
jwt-auth/
├── src/
│   ├── main/
│   │   ├── java/com/shumkar/jwt_auth/
│   │   │   ├── JwtAuthApplication.java          # Main application entry point
│   │   │   ├── config/                          # Configuration classes
│   │   │   │   ├── ApplicationConfiguration.java
│   │   │   │   ├── EmailConfiguration.java
│   │   │   │   ├── JwtAuthenticationFilter.java # Custom JWT filter
│   │   │   │   └── SecurityConfiguration.java   # Spring Security setup
│   │   │   ├── controller/                      # REST Controllers
│   │   │   │   ├── AuthenticationController.java
│   │   │   │   └── UserController.java
│   │   │   ├── dto/                             # Data Transfer Objects
│   │   │   │   ├── LoginUserDto.java
│   │   │   │   ├── RegisterUserDto.java
│   │   │   │   └── VerifyUserDto.java
│   │   │   ├── model/                           # Entity classes
│   │   │   │   └── User.java
│   │   │   ├── repository/                      # Data access layer
│   │   │   │   └── UserRepository.java
│   │   │   ├── responses/                       # Response objects
│   │   │   │   └── LoginResponse.java
│   │   │   └── service/                         # Business logic layer
│   │   │       ├── AuthenticationService.java
│   │   │       ├── EmailService.java
│   │   │       ├── JwtService.java
│   │   │       └── UserService.java
│   │   └── resources/
│   │       ├── application.properties           # Application configuration
│   │       ├── static/                          # Static resources
│   │       └── templates/                       # Email templates
│   └── test/
│       └── java/com/shumkar/jwt_auth/
│           └── JwtAuthApplicationTests.java
├── gradle/                                      # Gradle wrapper files
│   └── wrapper/
│       ├── gradle-wrapper.jar
│       └── gradle-wrapper.properties
├── build.gradle                                 # Gradle build configuration
├── settings.gradle                              # Gradle settings
├── gradlew                                      # Gradle wrapper script (macOS/Linux)
├── gradlew.bat                                  # Gradle wrapper script (Windows)
├── HELP.md                                      # Spring Boot generated help
└── README.md                                    # This file
```

### Directory Descriptions

| Directory | Purpose |
|-----------|---------|
| `config/` | Spring configuration classes including security and email setup |
| `controller/` | REST API endpoints for authentication and user management |
| `dto/` | Data Transfer Objects for API request/response bodies |
| `model/` | JPA Entity classes representing database tables |
| `repository/` | Spring Data JPA repositories for database operations |
| `responses/` | Response DTOs for API responses |
| `service/` | Business logic and service layer implementation |

## Building the Project

### Prerequisites
- Java 21 installed on your system
- Gradle 9.2.1 (included via gradle wrapper, no separate installation needed)

### Build Commands

Using the Gradle wrapper (recommended):

```bash
# macOS/Linux
./gradlew build

# Windows
gradlew.bat build
```

Or with locally installed Gradle:
```bash
gradle build
```

### Run the Application

```bash
# macOS/Linux
./gradlew bootRun

# Windows
gradlew.bat bootRun
```

## Configuration

### Environment Variables Required

The application uses environment variables for sensitive configuration. Create a `.env` file in the project root:

```properties
# Database Configuration
SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/jwt_auth_db
SPRING_DATASOURCE_USERNAME=postgres
SPRING_DATASOURCE_PASSWORD=your_password

# JWT Configuration
JWT_SECRET_KEY=your_secret_key_min_32_chars

# Email Configuration
SUPPORT_EMAIL=your-email@gmail.com
APP_PASSWORD=your_app_specific_password
```

### Application Properties

Key configuration in `src/main/resources/application.properties`:
- **Spring Application Name**: jwt-auth
- **JWT Expiration Time**: 3600000 ms (1 hour)
- **Database Dialect**: PostgreSQL
- **Mail Server**: Gmail SMTP (smtp.gmail.com:587)

## IntelliJ IDEA Setup

### Initial Setup
1. Open the project in IntelliJ IDEA
2. IntelliJ should automatically detect the Gradle project
3. Go to **File** → **Project Structure** → **Project** and verify:
   - **SDK**: Java 21
   - **Language Level**: 21

### Gradle Version Verification

If you encounter build errors, verify the Gradle version:

1. **File** → **Settings** → **Build, Execution, Deployment** → **Gradle**
2. Ensure **Use Gradle from** is set to:
   - **'gradle-wrapper.properties'** (Recommended) - Uses Gradle 9.2.1
   - Or specify the Gradle home pointing to Gradle 9.2.1

### Troubleshooting Build Errors

**Error**: "Could not determine Java version"
- **Solution**: Verify Java 21 is installed and set as the project SDK

**Error**: Gradle build fails with version mismatch
- **Solution**: 
  1. Delete the `~/.gradle/` cache directory
  2. Invalidate IDE caches: **File** → **Invalidate Caches**
  3. Rebuild the project

**Error**: Lombok annotations not recognized
- **Solution**: 
  1. Install Lombok plugin in IntelliJ: **Settings** → **Plugins** → search "Lombok"
  2. Enable annotation processing: **Settings** → **Build, Execution, Deployment** → **Compiler** → **Annotation Processors** → Check "Enable annotation processing"

## API Endpoints

### Authentication
- `POST /auth/signup` - Register a new user
- `POST /auth/login` - Login user
- `POST /auth/verify` - Verify user email

### User Management
- `GET /users/me` - Get current authenticated user
- `POST /users/` - Get all users

## Database Setup

The application uses PostgreSQL with Hibernate ORM. The schema is automatically created/updated based on the JPA entities.

Configure your database connection in the `.env` file and run the application. Hibernate will create the necessary tables.

## Testing

Run the test suite:

```bash
./gradlew test
```

## Additional Resources

- [Official Gradle Documentation](https://docs.gradle.org)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [JWT (jjwt) Library](https://github.com/jwtk/jjwt)
- [Spring Data JPA Documentation](https://spring.io/projects/spring-data-jpa)

## License

This project is provided as a template for JWT authentication systems.

## Notes

- The original package name was `com.shumkar.jwt-auth` but was changed to `com.shumkar.jwt_auth` (underscores instead of hyphens) to comply with Java naming conventions
- Always keep your JWT secret key secure and use environment variables
- For production deployments, ensure proper HTTPS, secure JWT secret generation, and email configuration

