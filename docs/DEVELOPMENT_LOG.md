# AI Document Assistant - Development Log

All developments, setup steps, architectural decisions, and code updates are logged chronologically in this file for future reference and project tracking.

---

## [2026-07-31] Project Context Analysis & Repository Audit

### Overview
- Analyzed the enterprise application requirements from `PROJECT_CONTEXT.md`.
- Evaluated existing codebase state across `backend/`, `frontend/`, `docker/`, and `docs/`.

### Findings
1. **Infrastructure**: `docker/docker-compose.yml` configured for PostgreSQL with `pgvector`, MinIO object storage, and Redis cache.
2. **Backend**: Maven project (`backend/pom.xml`) initialized with Spring Boot 3.5.6, Java 21, Spring Data JPA, Web, Validation, PostgreSQL driver, Lombok, and MinIO SDK.
3. **Frontend**: Directory created with an uninitialized `package.json`.
4. **Documentation**: `PROJECT_CONTEXT.md` defines system architecture, coding guidelines, package standards, and enterprise patterns.

### Actions Taken
- Established `DEVELOPMENT_LOG.md` structure for mandatory ongoing change logging.

---

## [2026-07-31] Step 3: Backend Core & Layered Architecture Setup

### Overview
Established the foundational classes for Spring Boot including MinIO integration config, CORS settings, custom exceptions, global exception advice, and unified response formats.

### Added Files
- [MinioConfig.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/config/MinioConfig.java): Spring configuration for `MinioClient` bean injection and auto-bucket creation checks.
- [WebMvcConfig.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/config/WebMvcConfig.java): CORS configuration mapping permitting communication with local frontend environments.
- [ApiResponse.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/dto/response/ApiResponse.java): General DTO response wrapper for consistent JSON response bodies.
- [BaseException.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/exception/BaseException.java): Base abstract class for custom runtime exception handling.
- [ResourceNotFoundException.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/exception/ResourceNotFoundException.java): Handled exception mapping to `404 Not Found`.
- [BusinessException.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/exception/BusinessException.java): General exception for logic rule violations mapping to `400 Bad Request`.
- [StorageException.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/exception/StorageException.java): Raw document storage/MinIO integration exception mapping to `500 Internal Server Error`.
- [GlobalExceptionHandler.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/exception/GlobalExceptionHandler.java): Advice intercepts errors, handles DTO constraints validation (`MethodArgumentNotValidException`), and maps unhandled exceptions safely.

### Verification
- Ran `./mvnw clean compile` showing `BUILD SUCCESS` with compilation of the 9 base Java files.

---

## [2026-07-31] Step 4: Document Domain Layer Setup

### Overview
Implemented the foundational Document Domain layer including JPA Entity mapping, Document repository interface, and Java records for upload request and response DTOs.

### Added Files
- [Document.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/entity/Document.java): JPA Entity representing the `documents` table with fields for tracking physical file object names, bucket, content type, upload status, size, and timestamp.
- [DocumentRepository.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/repository/DocumentRepository.java): Core database operations interface extending `JpaRepository`.
- [UploadDocumentRequest.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/dto/request/UploadDocumentRequest.java): Standardized Java record wrapping file upload payloads.
- [DocumentResponse.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/dto/response/DocumentResponse.java): API data envelope representing document details returned to the frontend.

### Verification
- Ran `.\mvnw clean compile` showing `BUILD SUCCESS` with compilation of 13 source files.

---

## [2026-07-31] Step 5: Document Upload Feature Implementation

### Overview
Implemented the full document upload feature. Clients can upload files which are verified against size limits and extensions, stored inside MinIO, cataloged inside PostgreSQL, and mapped back to the client response DTO. Exposed HTTP upload and search endpoints.

### Added/Modified Files
- [MinioProperties.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/config/MinioProperties.java): Scaffolds configuration class properties utilizing `@ConfigurationProperties` for externalized binding.
- [application.yml](file:///c:/AI-Document-Assistant/backend/src/main/resources/application.yml): Structured properties representation supporting complex bindings. Relocated database settings and storage configs from `application.properties`.
- [MinioConfig.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/config/MinioConfig.java): Refactored bean setup to inject `MinioProperties`.
- [ApplicationConstants.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/constant/ApplicationConstants.java): Centralizes max limits (100MB), supported extensions, and status constants.
- [FileUtil.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/util/FileUtil.java): Performs file verification (empty, extension patterns, size checks) and unique file naming logic.
- [DocumentMapper.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/mapper/DocumentMapper.java): Maps entity documents manually into response records.
- [DocumentService.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/service/DocumentService.java) & [DocumentServiceImpl.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/service/impl/DocumentServiceImpl.java): Handled validation, MinIO object streaming, JPA entity storage, and collection fetching.
- [DocumentController.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/controller/DocumentController.java): Exposes REST controllers under `/api/v1/documents`.

### Verification
- Executed `.\mvnw clean compile` showing `BUILD SUCCESS` with compilation of all 20 source files.

---

## [2026-08-04] Step 6: Text Extraction & Document Processing Integration

### Overview
Integrated Apache Tika to automatically extract text content from uploaded files (PDF, DOCX, TXT) immediately after they are successfully stored in MinIO. The extracted text is then persisted as metadata alongside the document records in PostgreSQL.

### Added/Modified Files
- [pom.xml](file:///c:/AI-Document-Assistant/backend/pom.xml): Added `tika-core` and `tika-parsers-standard-package` dependencies.
- [Document.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/entity/Document.java): Added `extractedText` field with column type defined as `TEXT`.
- [DocumentResponse.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/dto/response/DocumentResponse.java): Included `extractedText` field in the API response record.
- [DocumentMapper.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/mapper/DocumentMapper.java): Maps the new `extractedText` field from entity to response DTO.
- [DocumentParserUtil.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/util/DocumentParserUtil.java): Implemented static utility wrapping Tika's parser.
- [TextExtractionService.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/service/TextExtractionService.java) & [TextExtractionServiceImpl.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/service/impl/TextExtractionServiceImpl.java): Defined and implemented service class encapsulating extraction behavior with robust error fallbacks.
- [DocumentServiceImpl.java](file:///c:/AI-Document-Assistant/backend/src/main/java/com/sssk/backend/service/impl/DocumentServiceImpl.java): Updated upload flow to trigger parsing on the file input stream and save the text safely in the database.

### Verification
- Executed `.\mvnw clean compile` showing `BUILD SUCCESS` with compilation of all 23 source files.



