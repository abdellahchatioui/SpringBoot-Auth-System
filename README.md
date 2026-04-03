# Spring Security JWT Auth System
[![Java](https://img.shields.io/badge/Java-17%2B-orange?logo=java)](https://www.oracle.com/java/)
[![JWT](https://img.shields.io/badge/Authentication-JWT-black?logo=jsonwebtokens)](https://jwt.io/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6.x-brightgreen?logo=springboot)](https://spring.io/projects/spring-boot)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-4169E1?style=flat&logo=postgresql&logoColor=white)

A reusable authentication system built with Spring Boot and JWT. Handles user registration, login, and role-based access control with stateless security.

## Setup

**1. Clone the repository**
```bash
git clone https://github.com/abdellahchatioui/SpringBoot-Auth-System.git
cd SpringBoot-Auth-System
```

**2. Create the database**
```sql
CREATE DATABASE jwt_auth_db;
```

**3. Configure `application.properties`**
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/jwt_auth_db
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt.secret=your_secret_key_min_32_characters
jwt.expiration=86400000
```

**4. Run the application**
```bash
mvn spring-boot:run
```

---

## API Endpoints

| Method | Endpoint | Access |
|---|---|---|
| `POST` | `/auth/register` | Public |
| `POST` | `/auth/login` | Public |
| `GET` | `/user` | USER, ADMIN |
| `GET` | `/admin` | ADMIN only |
| `GET` | `/auth/test` | ADMIN only |

### Register
```json
POST /auth/register
{
  "username": "john",
  "email": "john@example.com",
  "password": "password123"
}
```

### Login
```json
POST /auth/login
{
  "username": "john",
  "password": "password123"
}
```
Returns a JWT token to use in subsequent requests.
---

## 👨‍💻 Author

**Abdellah Chatioui** *Fullstack Developer*

[![GitHub](https://img.shields.io/badge/GitHub-181717?style=for-the-badge&logo=github&logoColor=white)](https://github.com/abdellahchatioui)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/abdellah-chatioui-5b9426299/)



