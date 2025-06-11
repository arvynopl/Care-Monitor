# IF2050-2025-K4O-Care-Monitor

A desktop-based care monitoring system developed using Java Swing, PostgreSQL, and Gradle. This project enables efficient health data tracking and management for care-related applications.

## 📦 Prerequisites

- Java 11 or higher  
- Gradle installed (or use the provided wrapper `./gradlew`)

## ⚙️ Database Setup

To connect the application to a local PostgreSQL database, create a `.env` file in the project root based on `.env.example`:

```env
DB_URL=jdbc:postgresql://localhost:5432/caremonitor
DB_USER=postgres
DB_PASSWORD=your_postgres_password
````

These environment variables will be read by `DatabaseManager` at runtime.

> **Note:** If no `.env` file or environment variables are provided during tests, the system defaults to an in-memory H2 database.

## 🚀 Running the Application

Use Gradle to build and run the application:

```bash
./gradlew run
```

Or, if you have Gradle installed globally:

```bash
gradle run
```

## 🧪 Running Tests

The project uses **JUnit 5** and **Mockito** for unit testing.

To execute tests:

```bash
./gradlew test
```

### 🔄 Test Database Behavior

By default, tests run using an in-memory H2 database with PostgreSQL compatibility mode:

```text
jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1
```

You can override this configuration using environment variables:

```bash
DB_URL=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1 \
DB_USER=sa \
DB_PASSWORD= \
./gradlew test
```