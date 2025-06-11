# IF2050-2025-K4O-Care-Monitor

## Prerequisites

- Java 11 or higher
- Gradle installed or use the provided wrapper `./gradlew`

## Database Setup

Create a `.env` file in the project root based on `.env.example` and configure the database connection:

```env
DB_URL=jdbc:postgresql://localhost:5432/caremonitor
DB_USER=postgres
DB_PASSWORD=your_postgres_password
```

These variables are read by `DatabaseManager` when the application starts.

## Running the Application

Use Gradle to compile and launch the UI:

```bash
./gradlew run
```

If you have Gradle installed locally you can also run:

```bash
gradle run
```

## Running Tests

Execute the test suite with:

```bash
./gradlew test
```

or

```bash
gradle test
```

The tests use JUnit 5 and Mockito.
