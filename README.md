E-Banking Backend Application
Project Overview
This project is a Spring Boot application that provides a RESTful API for managing bank accounts, customers, and transactions. It also offers features to view transaction history. The project follows best practices such as using DTOs (Data Transfer Objects), mappers, and Spring annotations for code cleanliness and maintainability.

Features
Customer Management: Create, update, and delete customers.
Account Management: Create, update, delete bank accounts, and view account details.
Transaction Management: Perform debit and credit operations on accounts.
Transaction History: View the history of transactions for each account.
Exception Handling: Consistent error management using custom exceptions and global exception handlers.
Technologies
Java 17: The programming language used for development.
Spring Boot: Main framework used for building the application.
Spring Data JPA: For database interactions.
Spring Web: For creating REST APIs.
Hibernate: ORM framework used with JPA.
MySQL Database: In-memory database used for development and testing purposes.
Lombok: To reduce boilerplate code like getters, setters, constructors, etc.
OpenAPI/Swagger: For documenting the API.
Getting Started
Prerequisites
Java 17 installed
Maven for dependency management
IDE (IntelliJ, Eclipse, etc.) with Spring Boot support
Installation
Clone the repository:


Access the API documentation (Swagger):
(http://localhost:8081/swagger-ui/index.html)
