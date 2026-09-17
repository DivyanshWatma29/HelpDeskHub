# SupportDesk - Support Ticket Management System

SupportDesk is a simple academic project for managing support tickets. Users can raise a ticket for a problem, track it using a ticket number, and staff can update the ticket status.

## Live Demo

**[Click here to open the live demo](https://divyanshwatma29.github.io/SupportDesk/)**

The live demo is hosted on GitHub Pages, so it stays available to show the project flow. It demonstrates raising, tracking, and updating tickets in the browser. The main project uses React, Spring Boot, and MySQL.

## Objective

The objective of this project is to create a simple help desk system where users can report issues and support staff can manage them until they are resolved.

## Features

- Raise a support ticket with name, email, department, priority, and issue details
- Generate a ticket number for tracking
- Track a ticket using its ticket number
- View all tickets in a staff dashboard
- Change ticket status from Open to In Progress or Resolved
- Add a short resolution note
- Filter tickets by department, priority, or status

## Technologies Used

- React and Vite for the frontend
- Java and Spring Boot for the backend
- MySQL for data storage
- Spring Data JPA for database operations
- Spring Security for staff login

## Project Files

| Folder/File | Use |
| --- | --- |
| `frontend/` | React user interface for raising, tracking, and managing tickets. |
| `backend/` | Spring Boot API, ticket logic, and database connection. |
| `database/schema.sql` | Creates the MySQL tables. |
| `docs/index.html` | Always-live GitHub Pages demonstration. |

## Database Tables

| Table | Purpose |
| --- | --- |
| `departments` | Stores department names such as IT Support and HR. |
| `users` | Stores requester and staff information. |
| `tickets` | Stores ticket title, description, priority, status, and dates. |
| `audit_logs` | Stores simple history when a ticket is created or updated. |

## Ticket Flow

1. A user fills in the Raise Ticket form.
2. The system saves the ticket and generates a ticket number.
3. The user can enter that ticket number in Track Ticket to check its status.
4. A staff member opens the dashboard and changes the status when work starts or finishes.
5. The user can track the same ticket again to see the update.

## How to Run Locally

### Backend

Requirements: Java 21 and Maven.

```powershell
cd backend
$env:ADMIN_PASSWORD = "choose_an_admin_password"
$env:AGENT_PASSWORD = "choose_an_agent_password"
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The backend starts at `http://localhost:8080` using the H2 development database. Use username `admin` or `agent` with the password you set above to open the staff dashboard.

### Frontend

Requirements: Node.js and npm.

```powershell
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173` in a browser.

### MySQL Database

For MySQL, run `database/schema.sql` in MySQL Workbench. Then set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` before starting the backend without the `dev` profile.

## Points to Explain in Viva

- React is used to make the forms and dashboard interactive.
- Spring Boot provides REST API endpoints between the frontend and database.
- MySQL stores tickets permanently in tables.
- Each ticket has a unique ticket number so a user can track it.
- Ticket status shows whether work is Open, In Progress, or Resolved.
- The audit log keeps a simple record of ticket updates.
