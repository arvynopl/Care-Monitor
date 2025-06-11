# IF2050-2025-K4O-Care-Monitor

## Running Tests

The project uses an in-memory H2 database when running the unit tests.
`DatabaseManager` reads the connection information from the environment
variables `DB_URL`, `DB_USER` and `DB_PASSWORD`. If these variables are not
provided and there is no `.env` file, an in-memory H2 database
(`jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1`) is used by default.

Run the test suite with:

```bash
./gradlew test
```

You can override the database configuration:

```bash
DB_URL=jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1 \
DB_USER=sa \
DB_PASSWORD= \
./gradlew test
```
