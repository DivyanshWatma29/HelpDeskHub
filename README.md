# HelpDeskHub

**Enterprise-style IT Service Desk Management System** built with Java 21, Spring Boot, React, JPA/Hibernate, Spring Security, and MySQL.

HelpDeskHub models an internal service-desk workflow: employees submit workplace issues, support agents manage the ticket queue, administrators control privileged operations, and ticket lifecycle changes are retained in an audit trail.

**Live:** https://helpdeskhub-frontend.onrender.com  
**API health:** https://helpdeskhub-api.onrender.com/api/health

## 1. What the project does

HelpDeskHub is a full-stack ticket management application for internal IT/service operations.

Employees can submit tickets with requester information, department, priority, title, and description; receive a unique ticket number; and track ticket status and history without staff access.

Support agents can view and filter the ticket queue, update status, add resolution notes, and inspect the complete audit timeline. Administrators have the additional permission to delete tickets.

The ticket workflow is:

`OPEN → IN_PROGRESS → RESOLVED`

## 2. Technology stack

| Layer | Technology | Role |
|---|---|---|
| Frontend | React 19 | UI and application state |
| Frontend tooling | Vite | Development and production build |
| Styling | CSS | Responsive interface |
| Backend | Java 21 | Application language/runtime |
| API framework | Spring Boot 3.5 | REST application |
| Web/API | Spring Web | HTTP/JSON endpoints |
| Persistence | Spring Data JPA + Hibernate | ORM and database access |
| Security | Spring Security | Authentication and RBAC |
| Password hashing | BCrypt | Password hashing |
| Validation | Jakarta Bean Validation | Request validation |
| Database | MySQL 8 | Relational persistence |
| Development database | H2 | In-memory development profile |
| Backend build | Maven | Dependencies/build lifecycle |
| Frontend build | npm + Vite | Dependencies/build lifecycle |
| Deployment | Render | Backend container + static frontend |
| Version control | Git/GitHub | Source control |

## 3. Architecture

```text
┌───────────────────────────────┐
│          React + Vite         │
│ Employee Portal / Dashboard   │
└───────────────┬───────────────┘
                │ HTTP + JSON
                │ REST API
                ▼
┌───────────────────────────────┐
│       Spring Boot Backend     │
│                               │
│ Controller → Service → JPA    │
│     ↓             ↓           │
│ DTOs / Validation / Security  │
└───────────────┬───────────────┘
                │ Hibernate/JDBC
                ▼
┌───────────────────────────────┐
│           MySQL 8             │
│ Departments | Users           │
│ Tickets     | Audit Logs      │
└───────────────────────────────┘
```

The backend follows a layered design:

- **Controllers** expose REST resources and handle HTTP input/output.
- **Services** contain ticket workflow and business logic.
- **Repositories** provide database access through Spring Data JPA.
- **Entities/models** represent the relational domain.
- **DTOs** keep API contracts separate from persistence entities.
- **Security configuration** handles authentication and role-based authorization.
- **Exception handling** converts application failures into API responses.

## 4. Data flow

### Ticket creation

```text
Employee form
    ↓
React
    ↓ POST /api/tickets
Spring Controller
    ↓
Bean Validation
    ↓
TicketService
    ↓
Create/find requester + validate department
    ↓
Ticket entity
    ↓
JPA/Hibernate
    ↓
MySQL tickets table
    ↓
CREATE audit entry
    ↓
JSON response with ticket number
    ↓
React confirmation
```

### Ticket update

```text
Agent/Admin
    ↓ PUT /api/tickets/{id}
Spring Security
    ↓
Controller → TicketService
    ↓
Validate/update ticket
    ↓
Persist changes
    ↓
Write audit_logs record
    ↓
Return updated ticket
    ↓
React refreshes dashboard
```

The frontend API client centralizes HTTP requests, JSON handling, authentication headers, error handling, and the configurable `VITE_API_BASE` URL.

## 5. Database structure

The database is normalized around four main tables:

```text
DEPARTMENTS  1 ──────── * USERS
     │
     └─────────────── * TICKETS * ──────── 1 USERS (requester)
                              │
                              └─────────── 1 USERS (agent)
                              │
                              └─────────── * AUDIT_LOGS
```

### departments
Stores support/organizational departments. `name` and `code` are unique.

### users
Stores employee/requester records and roles. Email is unique and department membership is optional.

### tickets
Core transactional table containing ticket number, title, description, priority, status, department, requester, optional assignee, notes, and timestamps.

### audit_logs
Stores ticket lifecycle history: action, old status, new status, note, actor, and timestamp.

The schema uses foreign keys with `RESTRICT`, `SET NULL`, and `CASCADE` according to relationship semantics. Indexes are defined for common ticket filtering and lookup fields including department, assignee, status, priority, and creation time.

## 6. REST API

| Method | Endpoint | Purpose | Access |
|---|---|---|---|
| POST | `/api/tickets` | Create ticket | Public |
| GET | `/api/tickets` | List/filter tickets | Agent/Admin |
| GET | `/api/tickets/{id}` | Ticket + audit details | Agent/Admin |
| GET | `/api/tickets/track/{ticketNumber}` | Track ticket | Public |
| PUT | `/api/tickets/{id}` | Update status/note | Agent/Admin |
| DELETE | `/api/tickets/{id}` | Delete ticket | Admin |
| GET | `/api/departments` | Department lookup | Public |
| GET | `/api/dashboard/summary` | Dashboard aggregates | Agent/Admin |
| GET | `/api/auth/me` | Validate staff authentication | Authenticated |
| GET | `/api/health` | Health check | Public |

## 7. Authentication and authorization

Spring Security protects staff operations. Public users can submit and track tickets. Ticket management and dashboard operations require authentication.

Roles:

- `EMPLOYEE` — requester/domain role stored in the database.
- `AGENT` — can manage tickets.
- `ADMIN` — can manage tickets and perform privileged deletion operations.

Staff authentication currently uses HTTP Basic authentication with BCrypt password hashing. Staff credentials are configurable through application properties/environment variables rather than being embedded in the security configuration.

For a production deployment, credentials should be managed exclusively through the hosting provider's secret/environment management.

## 8. Algorithms, models, and engineering mechanisms

This is **not an ML project** and intentionally has no trained machine-learning model. It is a relational business application where deterministic workflow rules and indexed database queries are appropriate.

Important mechanisms:

1. **3NF relational normalization** — separates departments, users, tickets, and audit records to reduce duplication.
2. **B-Tree database indexes** — accelerate common filtering and lookup operations.
3. **REST routing** — maps HTTP methods/resources to controller operations.
4. **Role-Based Access Control (RBAC)** — restricts operations according to authenticated role.
5. **Ticket state machine/workflow** — operational states are constrained to `OPEN`, `IN_PROGRESS`, and `RESOLVED`.
6. **Audit logging** — records lifecycle changes so ticket history can be reconstructed.
7. **DTO-based API contracts** — avoids exposing persistence entities directly through the API.

## 9. Testing and evaluation

The repository now includes CI build validation for both backend and frontend.

### Automated CI

On pushes and pull requests to `main`:

- Backend runs `mvn test` on Java 21.
- Frontend runs `npm install` and `npm run build` on Node.js 20.

### Functional evaluation

| Area | Test/evaluation |
|---|---|
| Build | Maven test/build + Vite production build |
| API | REST requests through Postman/curl |
| Authentication | Valid and invalid staff credentials |
| Authorization | Agent vs Admin permission checks |
| Validation | Missing/invalid ticket fields |
| Database integrity | Foreign keys, unique constraints, indexes |
| Workflow | OPEN → IN_PROGRESS → RESOLVED |
| Auditability | Verify lifecycle actions create audit records |
| Filtering | Department/status/priority combinations |
| Deployment | `/api/health` + frontend smoke test |

Dedicated JUnit/Mockito and frontend component test coverage should be expanded next; do not claim a test-coverage percentage until those tests exist.

## 10. Local development

### Requirements

- Java 21
- Maven 3.9+
- Node.js 18+
- npm
- MySQL 8 for the production-like profile

H2 is available through the development profile for an offline/local backend run.

### Backend

```bash
cd backend
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

Backend: `http://localhost:8080`

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Frontend: `http://localhost:5173`

To point the frontend at a custom API, create `frontend/.env.local`:

```env
VITE_API_BASE=http://localhost:8080/api
```

## 11. Project structure

```text
HelpDeskHub/
├── .github/workflows/ci.yml
├── backend/
│   ├── src/main/java/com/helpdeskhub/
│   │   ├── config/
│   │   ├── controller/
│   │   ├── dto/
│   │   ├── exception/
│   │   ├── model/
│   │   ├── repository/
│   │   └── service/
│   ├── Dockerfile
│   └── pom.xml
├── database/
│   └── schema.sql
├── frontend/
│   ├── src/
│   └── package.json
├── render.yaml
└── README.md
```

## 12. Resume-ready description

**HelpDeskHub — Enterprise IT Service Desk Management System**

Built a full-stack IT service desk platform using **Java 21, Spring Boot, React, JPA/Hibernate, Spring Security, and MySQL**, implementing role-based ticket management, filterable ticket queues, public ticket tracking, normalized relational data modeling, and lifecycle audit logging. Designed RESTful APIs and a 3NF database with indexed ticket fields and referential integrity constraints, with separate frontend/backend deployment.

## 13. Recommended next upgrades

- Add JUnit/Mockito unit tests for `TicketService`.
- Add Spring Boot integration tests for public/protected endpoints.
- Add frontend tests with Vitest and React Testing Library.
- Add API smoke tests to CI.
- Add pagination for large ticket queues.
- Add Flyway/Liquibase database migrations.
- Add structured logging and request correlation IDs.
- Add Docker Compose for local MySQL + backend.
- Replace HTTP Basic with a production-oriented authentication flow when the application moves beyond a portfolio/demo deployment.

## License

Portfolio/academic project.
