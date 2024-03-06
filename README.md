# JWT Authentication and Authorization without Spring Security

This project demonstrates how to implement authentication and authorization using JSON Web Tokens (JWT) without relying on Spring Security.

## Introduction

The project consists of the following components:

1. **RegistrationController**: REST controller responsible for user registration and authentication endpoints.
2. **TestAuthController**: Example controller with protected endpoints that require authentication.
3. **AuthenticationFilter**: Filter responsible for authenticating incoming requests by validating JWT tokens.
4. **AuthorizationFilter**: Filter responsible for authorizing access to protected endpoints based on user roles.
5. **User**: Entity representing a user in the system.
6. **UserRole**: Enum defining user roles.
7. **JwtProvider**: Service responsible for JWT token generation and validation.
8. **AuthService**: Service handling user authentication and registration logic.
9. **UserService**: Service providing user-related functionalities like user creation and retrieval.

## Usage

To use this project:

1. **Setup Database**: Ensure you have a database configured and update the database configurations in `application.properties` file.

2. **Run Application**: Run the application using your preferred method (e.g., `mvn spring-boot:run`).

3. **Endpoints**:
   - `/api/v1/auth/sign-up`: POST endpoint for user registration.
   - `/api/v1/auth/sign-in`: POST endpoint for user authentication.
   - `/api/v1/hello`: GET endpoint accessible to all authenticated users.
   - `/api/v1/hello/admin`: GET endpoint accessible only to users with the role `ROLE_ADMIN`.

## Configuration

Make sure to configure the following properties in `application.properties`:
- `jwtToken.signingKey`: Signing key for JWT token.
- `jwtToken.lifetime`: Lifetime of JWT token.

## Dependencies

Ensure you have the following dependencies in your `pom.xml`:
- Spring Boot Starter Web
- Spring Boot Starter Data JPA
- H2 Database (or any other database of your choice)
- Auth0 JWT Library

## Conclusion

This project provides a basic implementation of authentication and authorization using JWT in a Spring Boot application without relying on Spring Security. Feel free to extend and customize it according to your requirements.

