# AI Document Assistant - Project Context

---

# 1. Project Overview

AI Document Assistant is a production-grade, enterprise-level full-stack application that enables users to securely upload, manage, analyze, search, and interact with documents using Artificial Intelligence.

The application combines modern backend development, cloud-native infrastructure, AI services, document processing, vector search, and responsive frontend technologies to simulate how enterprise software is designed and developed in real-world organizations.

The project is intended to demonstrate professional software engineering practices, clean architecture, scalable system design, and production-ready implementation.

---

# 2. Project Objectives

The primary objectives of this project are:

* Build a complete enterprise-grade full-stack application.
* Learn every technology by implementing it from scratch.
* Follow clean architecture and SOLID principles.
* Build production-quality code rather than tutorial-style implementations.
* Understand enterprise software development workflows.
* Gain hands-on experience with AI integration.
* Develop a portfolio-quality project suitable for technical interviews and professional use.

---

# 3. System Overview

The application consists of two primary systems.

## Frontend

Responsible for:

* User Interface
* User Authentication Flow
* Dashboard
* Document Upload
* Document Management
* AI Chat Interface
* Search Interface
* User Experience

---

## Backend

Responsible for:

* REST APIs
* Authentication
* Authorization
* Business Logic
* Document Processing
* Metadata Management
* AI Integration
* Database Operations
* Storage Integration
* Security
* Validation

---

# 4. Technology Stack

## Frontend

* React.js
* TypeScript
* Vite
* Tailwind CSS
* React Router
* Axios
* React Query (if required)

---

## Backend

* Java 21
* Spring Boot 3.5.x
* Spring MVC
* Spring Data JPA
* Spring Validation
* Spring Security
* Maven

---

## Database

* PostgreSQL
* pgvector

---

## Object Storage

* MinIO

---

## Cache

* Redis

---

## AI & Document Processing

* Spring AI
* OpenAI / Ollama
* Apache Tika
* OCR Engine
* Embedding Models
* Vector Search

---

## Infrastructure

* Docker
* Docker Compose

---

## Development Tools

* IntelliJ IDEA
* VS Code
* Git
* GitHub
* Postman

---

# 5. High-Level Architecture

User

↓

React Frontend

↓

REST APIs

↓

Spring Boot Backend

↓

Business Layer

↓

Persistence Layer

↓

PostgreSQL

↓

MinIO

↓

Redis

↓

Spring AI / LLM

The frontend communicates only through REST APIs.

The backend owns all business logic.

The database stores metadata.

MinIO stores physical files.

Redis stores temporary/cache data.

AI services are isolated behind dedicated service classes.

---

# 6. Repository Structure

AI-Document-Assistant/

backend/

frontend/

docker/

docs/

README.md

.gitignore

---

# 7. Backend Package Structure

com.sssk.backend

config

controller

service

service.impl

repository

entity

dto

request

response

mapper

exception

security

util

constant

---

# 8. Frontend Structure

src/

components/

pages/

layouts/

routes/

hooks/

services/

context/

assets/

types/

utils/

The frontend should remain modular and component-driven.

Business logic should remain in the backend whenever possible.

---

# 9. Architecture Principles

The application must always follow:

* Layered Architecture
* Separation of Concerns
* SOLID Principles
* Clean Code
* Dependency Injection
* Constructor Injection
* Interface-Based Design
* Reusable Components
* Modular Development
* Loose Coupling
* High Cohesion

---

# 10. Coding Standards

Every class should have a single responsibility.

Controllers should only handle HTTP requests and responses.

Business logic belongs only in Services.

Repositories should only access the database.

Configurations belong only inside configuration classes.

Entities should represent database tables.

DTOs should represent API contracts.

Validation should be performed on DTOs.

Meaningful names should be used for all classes, methods, and variables.

Readable code is preferred over complex code.

Avoid duplicate implementations.

---

# 11. API Design Standards

The application should follow RESTful API principles.

Use nouns instead of verbs.

Use appropriate HTTP methods.

Use standard HTTP status codes.

Return consistent response structures.

Validate every request.

Handle exceptions globally.

Use API versioning whenever necessary.

Example:

/api/v1/auth

/api/v1/users

/api/v1/documents

/api/v1/chat

---

# 12. Database Standards

Every table must have a primary key.

Relationships should be properly normalized.

Store document metadata only.

Store uploaded files in MinIO.

Use indexes where required.

Follow consistent naming conventions.

Avoid redundant data.

---

# 13. Object Storage Standards

All uploaded files must be stored inside MinIO.

The database stores only metadata.

Generate unique object names.

Never expose storage implementation details to API consumers.

Organize files using logical bucket structures.

---

# 14. Security Standards

Authentication should use JWT.

Passwords should be encrypted.

Role-based authorization should be implemented.

Sensitive APIs must be protected.

Validate all incoming input.

Never expose sensitive internal information.

---

# 15. Exception Handling

Use Global Exception Handling.

Create custom exceptions where appropriate.

Return meaningful error responses.

Use proper HTTP status codes.

Do not expose stack traces to clients.

---

# 16. Validation Standards

Validate request DTOs.

Use Bean Validation annotations.

Keep validation outside business logic.

Return user-friendly validation messages.

---

# 17. Docker Standards

Infrastructure services should run inside Docker.

Docker Compose manages project dependencies.

Development and deployment environments should remain consistent.

---

# 18. Git Standards

Commit small logical changes.

Write meaningful commit messages.

Keep the repository organized.

Avoid committing generated files.

Do not commit secrets or credentials.

---

# 19. Documentation Standards

Every important feature should be documented.

Architectural decisions should be recorded.

API documentation should remain updated.

Project documentation should evolve with the project.

---

# 20. AI Assistant Guidelines

Any AI assistant contributing to this project must follow these principles:

* Treat this as an enterprise software project.
* Continue from the existing architecture.
* Never redesign the application unless explicitly requested.
* Explain concepts before implementation.
* Explain architectural decisions.
* Generate production-quality code.
* Follow Spring Boot best practices.
* Follow React best practices.
* Keep frontend and backend responsibilities separate.
* Avoid unnecessary libraries.
* Keep implementations modular.
* Prefer maintainability over shortcuts.
* Do not skip implementation steps.
* Assume the developer wants to understand every concept before writing code.

---

# 21. Development Philosophy

Learning is the highest priority.

Every feature should be implemented with complete understanding.

Architecture should always take priority over speed.

Every implementation should remain scalable, maintainable, and extensible.

The project should continuously evolve toward production-quality software.

---

# 22. Do's

* Build features incrementally.
* Maintain clean architecture.
* Keep frontend and backend loosely coupled.
* Write reusable code.
* Follow enterprise standards.
* Test completed features.
* Document important decisions.
* Keep the project modular.

---

# 23. Don'ts

* Do not skip architectural layers.
* Do not place business logic inside controllers.
* Do not expose entities directly through APIs.
* Do not tightly couple frontend and backend.
* Do not introduce unnecessary dependencies.
* Do not duplicate code.
* Do not mix responsibilities across layers.
* Do not make architectural changes without clear justification.
* Do not sacrifice readability for shorter code.

---

# 24. Long-Term Vision

The long-term vision is to build a scalable, secure, AI-powered enterprise document platform that demonstrates modern software engineering practices, clean architecture, cloud-native infrastructure, AI integration, and production-ready full-stack development.

Every new feature, module, and architectural decision should align with this vision and preserve the overall consistency, maintainability, and quality of the application.
