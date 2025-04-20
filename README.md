##  Overview
**OptiManage** is a Spring Boot-based Employee & HR Management system that facilitates:
- Employee and department management
- Leave request handling with managerial approvals
- Project and meeting tracking
- Salary and performance management
- Investment and investor tracking
- Basic JWT authentication and authorization

---

##  Technologies Used

- **Java 11+**
- **Spring Boot**
- **Spring Security (JWT-based)**
- **MySQL**
- **Maven**
- **REST APIs**

---

## Project Structure (Key Modules)

```
src/
└── main/java/com/example/demo
    ├── config/             # Security config & JWT filter
    ├── controller/         # REST controllers
    ├── dao/                # DAO/Repository interfaces
    ├── dto/                # Data Transfer Objects
    ├── entity/             # JPA entity classes
    └── DemoApplication.java
```

---

## Database Setup

the .sql file is given. You can use it to create the database schema.

##  Authentication

- JWT is used for API authentication.
- User credentials and tokens are handled via `AuthController.java`.
- `SecurityConfig.java` sets protected endpoints.
- For now, login may be hardcoded in development (e.g. `employee_id = 2 or 3` in leave request).

---

## API Endpoints (Key Controllers)

### `/auth`
- `POST /auth/login` — Authenticate user and return JWT.

### `/employee`
- `GET /employee/all` — Get all employees.
- `POST /employee/add` — Add new employee.
- `PUT /employee/update/{id}` — Update employee info.

### `/department`
- `GET /department/all` — Get departments.
- `POST /department/add` — Add a department.

### `/leave/request`
- `POST /leave/request/add` — Add leave request for logged-in employee.
- `GET /leave/request/all` — Get all leave requests (admin).
- `GET /leave/request/all2` — Get leave requests of subordinates (for manager).

### `/project`, `/meeting`, `/performance`, `/salary`, `/investment`, `/investor`
- Each handles CRUD for their respective data types.

---

## How to Run

1. **Set up the database**:
   - Use MySQL.
   - Run the SQL commands in `db queries.sql`.

2. **Configure application.properties** (not shown, but expected in `resources/`)
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/your_db
   spring.datasource.username=your_user
   spring.datasource.password=your_password
   ```

3. **Run with Maven**:
   ```bash
   ./mvnw spring-boot:run
   ```
   ```
   dor windows :
   mvn spring-boot:run

4. **Use Postman or Swagger** to test APIs.

---

## Developer Notes

- The leave request logic is currently hardcoded in some areas (e.g., manager ID = 1).
- Authentication is in place but may need proper user login form/frontend.
- Make sure to assign managers by setting `reports_to` field for employees.

---

