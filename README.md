# K12 Management System

A full-stack school management system built for Philippine K-12 institutions, following DepEd standards including trimester-based grading and transmutation logic.

---

## 📚 Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Tech Stack](#tech-stack)
- [System Architecture](#system-architecture)
- [Getting Started](#getting-started)
  - [Prerequisites](#prerequisites)
  - [Running Locally](#running-locally)
  - [Running with Docker](#running-with-docker)
- [API Endpoints](#api-endpoints)
- [Grading System](#grading-system)
- [Project Structure](#project-structure)

---

## Overview

The K12 Management System is a backend-heavy REST API system designed to manage the core operations of a K-12 school — from student registration and enrollment to teacher scheduling and grade encoding. It follows Philippine DepEd conventions including trimester grading periods and the official transmutation table.

---

## Features

### 👤 Authentication
- JWT-based authentication with access and refresh tokens
- DB-backed refresh tokens with rotation and revocation
- Role-based access: `STUDENT`, `TEACHER`, `REGISTRAR`, `PRINCIPAL`, `SECRETARY`
- Automatic PDF credential generation on account creation

### 🎓 Student Management
- Student self-registration with `PENDING` status
- Registrar approval to activate student accounts
- Auto-generated student numbers in format `{year}-G{gradeLevel}-{sequence}`
- PDF credential generation via iText7

### 📋 Student Enrollment
- Registrar-managed enrollment with automatic status sync
- Room capacity validation
- Student transfer between sections
- Enrollment status tracking: `ACTIVE`, `DROPPED`, `TRANSFERRED`, `INACTIVE`, `GRADUATED`
- Student status auto-syncs with enrollment status changes

### 🏫 Section Management
- Section creation with room and adviser assignment
- Room capacity enforcement
- School year scoping

### 📅 Teacher Scheduling
- Multi-day schedule creation in a single request
- Conflict detection for teacher and section time slots
- Schedule status management: `ACTIVE`, `CANCELLED`
- Student class schedule retrieval via enrollment

### 📝 Grading System
- Trimester-based grading: `TRIMESTER_1`, `TRIMESTER_2`, `TRIMESTER_3`
- Three grading components:
  - Written Works (WW) — 25%
  - Performance Tasks (PT) — 50%
  - Quarterly Assessment (QA) — 25%
- Auto-computed initial grade and transmuted grade (DepEd transmutation table)
- Final grade computed as average of all 3 trimesters
- Separate `GradingCriteria` entity for reusable score totals per section/subject

### 🏢 Employee Management
- Employee registration with role assignment
- PDF credential generation
- Status management: `ACTIVE`, `INACTIVE`

### 🗂️ Supporting Modules
- School management
- Room management
- Subject and Subject Area management
- User account management with password hashing

---

## Tech Stack

### Backend
| Technology | Purpose |
|---|---|
| Kotlin | Primary language |
| Spring Boot 4.x | Application framework |
| Spring Data JPA / Hibernate | ORM and database access |
| Spring Security | Authentication and authorization |
| JWT (jjwt) | Token generation and validation |
| iText7 | PDF generation |
| PostgreSQL 18 | Database |
| HikariCP | Connection pooling |
| Docker | Containerization |

### Frontend
| Technology | Purpose |
|---|---|
| Angular 22 | Frontend framework |
| TypeScript | Primary language |
| Angular Signals | State management |

---

## System Architecture

```
┌─────────────────┐        ┌─────────────────┐        ┌─────────────────┐
│   Angular 22    │──────▶│  Spring Boot 4  │──────▶│  PostgreSQL 18  │
│  localhost:4200 │        │  localhost:8080 │        │  localhost:5434 │
└─────────────────┘        └─────────────────┘        └─────────────────┘
        │                          │
        │  /api proxy              │  PDF output
        │                          ▼
        │                  ┌─────────────────┐
        │                  │ accounts/        │
        │                  │ ├── students/    │
        │                  │ └── employees/   │
        │                  └─────────────────┘
        └─────────────────────────────────────
```

**Layered Architecture (per entity):**
```
Controller → Service Interface → Service Impl → Repository → Database
                    ↑
                  Mapper
                (DTO ↔ Entity)
```

---

## Getting Started

### Prerequisites

- Java 21
- Kotlin
- Node.js & Angular CLI
- Docker Desktop
- PostgreSQL 18 (for local development)
- IntelliJ IDEA (recommended)

### Running Locally

**1. Clone the repository:**
```bash
git clone https://github.com/your-username/k12-management-system.git
cd k12-management-system
```

**2. Configure `application.properties`:**
```properties
spring.datasource.url=${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/k12db}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME:postgres}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD:your_password}
jwt.secret=${JWT_SECRET:your_256bit_secret_key}
```

**3. Run the backend:**
```bash
./gradlew bootRun
```

**4. Run the frontend:**
```bash
cd frontend
ng serve
```

**5. Access the app:**
```
http://localhost:4200
```

---

### Running with Docker

**1. Build the Spring Boot jar:**
```bash
./gradlew bootJar
```

**2. Start all services:**
```bash
docker-compose up -d
```

**3. Stop all services:**
```bash
docker-compose down
```

**4. Rebuild after code changes:**
```bash
./gradlew bootJar && docker-compose down && docker-compose up -d --build
```

**Docker services:**
| Service | Port |
|---|---|
| Spring Boot Backend | 8080 |
| PostgreSQL | 5434 |

---

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/auth/login` | Login and get tokens |
| POST | `/api/auth/refresh` | Refresh access token |
| POST | `/api/auth/logout` | Logout and revoke token |

### Students
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/students/register` | Register new student |
| PATCH | `/api/students/{id}/activate` | Activate student account |
| GET | `/api/students/{id}` | Get student by ID |
| GET | `/api/students/user/{userId}` | Get student by user ID |
| GET | `/api/students/number/{studentNumber}` | Get student by number |
| GET | `/api/students/school/{schoolId}` | Get all students by school |
| GET | `/api/students/school/{schoolId}/list` | Get student list (lightweight) |
| GET | `/api/students/pending/{schoolId}` | Get pending students |

### Enrollment
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/enrollments` | Create enrollment |
| PATCH | `/api/enrollments/{id}/status` | Update enrollment status |
| PATCH | `/api/enrollments/{id}/transfer` | Transfer student to new section |
| GET | `/api/enrollments/{id}` | Get enrollment by ID |
| GET | `/api/enrollments/school/{schoolId}` | Get by school |
| GET | `/api/enrollments/student/{studentId}` | Get by student |
| GET | `/api/enrollments/section/{sectionId}` | Get by section |
| GET | `/api/enrollments/school/{schoolId}/school-year` | Get by school year |
| GET | `/api/enrollments/school/{schoolId}/status` | Get by status |

### Teacher Schedules
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/schedules` | Create schedule (multi-day) |
| PUT | `/api/schedules/{id}` | Update schedule |
| PATCH | `/api/schedules/{id}/status` | Update schedule status |
| GET | `/api/schedules/student/{studentId}` | Get student class schedule |
| GET | `/api/schedules/teacher/{employeeId}` | Get by teacher |
| GET | `/api/schedules/section/{sectionId}` | Get by section |
| GET | `/api/schedules/subject/{subjectId}` | Get by subject |
| DELETE | `/api/schedules/{id}` | Delete schedule |

### Grading
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/grading-criteria` | Create grading criteria |
| PUT | `/api/grading-criteria/{id}` | Update criteria |
| GET | `/api/grading-criteria/{id}` | Get by ID |
| GET | `/api/grading-criteria/section/{sectionId}` | Get by section |
| DELETE | `/api/grading-criteria/{id}` | Delete criteria |
| POST | `/api/grades` | Encode grade |
| PUT | `/api/grades/{id}` | Update grade |
| GET | `/api/grades/student/{studentId}` | Get grades by student |
| GET | `/api/grades/student/{studentId}/final` | Get final grades |
| GET | `/api/grades/section/{sectionId}` | Get grades by section |
| DELETE | `/api/grades/{id}` | Delete grade |

### Employees
| Method | Endpoint | Description |
|---|---|---|
| POST | `/api/employees` | Create employee |
| PUT | `/api/employees/{id}` | Update employee |
| GET | `/api/employees/{id}` | Get by ID |
| GET | `/api/employees/school/{schoolId}` | Get by school |
| DELETE | `/api/employees/{id}` | Delete employee |

---

## Grading System

Follows DepEd K-12 grading guidelines:

### Components
| Component | Weight |
|---|---|
| Written Works (WW) | 25% |
| Performance Tasks (PT) | 50% |
| Quarterly Assessment (QA) | 25% |

### Computation
```
WW Score  = (writtenWorksScore  / writtenWorksTotal)  × 25
PT Score  = (performanceTaskScore / performanceTaskTotal) × 50
QA Score  = (quarterlyAssessmentScore / quarterlyAssessmentTotal) × 25

Initial Grade   = WW Score + PT Score + QA Score
Transmuted Grade = DepEd transmutation table applied to Initial Grade
Final Grade      = (T1 + T2 + T3) / 3
```

### Grading Scale
| Transmuted Grade | Description |
|---|---|
| 90 - 100 | Outstanding |
| 85 - 89 | Very Satisfactory |
| 80 - 84 | Satisfactory |
| 75 - 79 | Fairly Satisfactory |
| Below 75 | Did Not Meet Expectations |

---

## Project Structure

```
k12/
├── src/
│   └── main/
│       └── kotlin/com/schoolproject/k12/
│           ├── config/          # Security, JWT, CORS config
│           ├── controller/      # REST controllers
│           ├── dto/
│           │   ├── request/     # Request DTOs
│           │   └── response/    # Response DTOs
│           ├── entity/          # JPA entities
│           ├── mapper/          # Entity ↔ DTO mappers
│           ├── model/           # Enums and models
│           ├── repository/      # Spring Data JPA repositories
│           └── service/
│               ├── impl/        # Service implementations
│               └── *.kt         # Service interfaces
├── Dockerfile
├── docker-compose.yml
├── build.gradle.kts
└── accounts/
    ├── students/                # Generated student PDFs
    └── employees/               # Generated employee PDFs
```

