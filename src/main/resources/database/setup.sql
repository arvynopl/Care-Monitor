-- -- Create database (run this in PostgreSQL CLI as superuser)
-- CREATE DATABASE caremonitor;

-- -- Create user for the application (optional, for security)
-- CREATE USER caremonitor_user WITH PASSWORD 'caremonitor_pass';
-- GRANT ALL PRIVILEGES ON DATABASE caremonitor TO caremonitor_user;

-- -- Connect to the caremonitor database
-- \c caremonitor;

-- -- Create tables
-- CREATE TABLE IF NOT EXISTS users (
--     id SERIAL PRIMARY KEY,
--     full_name VARCHAR(100) NOT NULL,
--     email VARCHAR(100) UNIQUE NOT NULL,
--     password VARCHAR(100) NOT NULL,
--     role VARCHAR(20) NOT NULL,
--     specialization VARCHAR(100),
--     contact VARCHAR(50),
--     relationship VARCHAR(50),
--     last_login TIMESTAMP
-- );

-- CREATE TABLE IF NOT EXISTS patients (
--     id SERIAL PRIMARY KEY,
--     name VARCHAR(100) NOT NULL,
--     age INTEGER NOT NULL,
--     gender VARCHAR(10) NOT NULL,
--     address TEXT NOT NULL,
--     emergency_contact VARCHAR(50) NOT NULL,
--     unique_code VARCHAR(20) UNIQUE NOT NULL,
--     caregiver_id INTEGER REFERENCES users(id) ON DELETE SET NULL
-- );

-- CREATE TABLE IF NOT EXISTS family_patient (
--     family_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
--     patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
--     PRIMARY KEY (family_id, patient_id)
-- );

-- CREATE TABLE IF NOT EXISTS health_data (
--     id SERIAL PRIMARY KEY,
--     patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
--     heart_rate DOUBLE PRECISION NOT NULL,
--     blood_pressure VARCHAR(20) NOT NULL,
--     temperature DOUBLE PRECISION NOT NULL,
--     position VARCHAR(50),
--     timestamp TIMESTAMP NOT NULL
-- );

-- CREATE TABLE IF NOT EXISTS critical_parameters (
--     id SERIAL PRIMARY KEY,
--     patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
--     min_heart_rate DOUBLE PRECISION NOT NULL,
--     max_heart_rate DOUBLE PRECISION NOT NULL,
--     min_blood_pressure VARCHAR(20) NOT NULL,
--     max_blood_pressure VARCHAR(20) NOT NULL,
--     min_temperature DOUBLE PRECISION NOT NULL,
--     max_temperature DOUBLE PRECISION NOT NULL
-- );

-- -- Grant permissions to application user
-- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO caremonitor_user;
-- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO caremonitor_user;

-- Create database (run this in PostgreSQL CLI as superuser)
CREATE DATABASE caremonitor;

-- Create user for the application (optional, for security)
CREATE USER caremonitor_user WITH PASSWORD 'caremonitor_pass';
GRANT ALL PRIVILEGES ON DATABASE caremonitor TO caremonitor_user;

-- Connect to the caremonitor database
\c caremonitor;

-- Create tables
CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    specialization VARCHAR(100),
    contact VARCHAR(50),
    relationship VARCHAR(50),
    last_login TIMESTAMP
);

CREATE TABLE IF NOT EXISTS patients (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    age INTEGER NOT NULL,
    gender VARCHAR(10) NOT NULL,
    address TEXT NOT NULL,
    emergency_contact VARCHAR(50) NOT NULL,
    unique_code VARCHAR(20) UNIQUE NOT NULL,
    caregiver_id INTEGER REFERENCES users(id) ON DELETE SET NULL
);

-- PERBAIKAN: Update tabel family_patient dengan kolom tambahan
CREATE TABLE IF NOT EXISTS family_patient (
    id SERIAL PRIMARY KEY,
    family_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
    full_name VARCHAR(100),
    email VARCHAR(100),
    contact VARCHAR(50),
    relationship VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(family_id, patient_id)
);

-- TAMBAHAN: Tabel caregiver untuk menyimpan informasi caregiver dan pasien yang ditangani
CREATE TABLE IF NOT EXISTS caregivers (
    id SERIAL PRIMARY KEY,
    user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    specialization VARCHAR(100),
    patient_ids INTEGER[],
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS health_data (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
    heart_rate DOUBLE PRECISION NOT NULL,
    blood_pressure VARCHAR(20) NOT NULL,
    temperature DOUBLE PRECISION NOT NULL,
    position VARCHAR(50),
    timestamp TIMESTAMP NOT NULL
);

CREATE TABLE IF NOT EXISTS critical_parameters (
    id SERIAL PRIMARY KEY,
    patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE,
    min_heart_rate DOUBLE PRECISION NOT NULL,
    max_heart_rate DOUBLE PRECISION NOT NULL,
    min_blood_pressure VARCHAR(20) NOT NULL,
    max_blood_pressure VARCHAR(20) NOT NULL,
    min_temperature DOUBLE PRECISION NOT NULL,
    max_temperature DOUBLE PRECISION NOT NULL
);

-- Grant permissions to application user
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO caremonitor_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO caremonitor_user;