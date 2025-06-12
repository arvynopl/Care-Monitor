package com.caremonitor.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class DatabaseManager {
    private static String URL;
    private static String USER;
    private static String PASSWORD;
    
    private static Connection connection;
    private static DatabaseManager instance;
    
    private DatabaseManager() {
        loadEnvVariables();
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            initializeDatabase();
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Database connection failed: " + e.getMessage());
        }
    }
    
    private void loadEnvVariables() {
        Map<String, String> env = new HashMap<>();

        // Load from .env file if present
        env.putAll(readEnvFile());

        // Override with actual environment variables when available
        env.putAll(System.getenv());

        URL = env.getOrDefault(
                "DB_URL",
                "jdbc:h2:mem:testdb;MODE=PostgreSQL;DB_CLOSE_DELAY=-1"
        );
        USER = env.getOrDefault("DB_USER", "sa");
        PASSWORD = env.getOrDefault("DB_PASSWORD", "");
    }
    
    private Map<String, String> readEnvFile() {
        Map<String, String> envVars = new HashMap<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(".env"));
            for (String line : lines) {
                line = line.trim();
                if (!line.isEmpty() && !line.startsWith("#")) {
                    String[] parts = line.split("=", 2);
                    if (parts.length == 2) {
                        String key = parts[0].trim();
                        String value = parts[1].trim();
                        if (value.startsWith("\"") && value.endsWith("\"")) {
                            value = value.substring(1, value.length() - 1);
                        }
                        envVars.put(key, value);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: .env file not found or cannot be read. Using default values.");
        }
        return envVars;
    }
    
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }
    
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
    
    private void initializeDatabase() {
        try {
            if (!tablesExist()) {
                createTables();
                seedData();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private boolean tablesExist() throws SQLException {
        DatabaseMetaData meta = connection.getMetaData();
        ResultSet tables = meta.getTables(null, null, "users", null);
        return tables.next();
    }
    
    private void createTables() throws SQLException {
        Statement stmt = connection.createStatement();
        
        stmt.execute("CREATE TABLE users (" +
                "id SERIAL PRIMARY KEY, " +
                "full_name VARCHAR(100) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "password VARCHAR(100) NOT NULL, " +
                "role VARCHAR(20) NOT NULL, " +
                "specialization VARCHAR(100), " +
                "contact VARCHAR(50), " +
                "relationship VARCHAR(50), " +
                "last_login TIMESTAMP" +
                ")");
        
        stmt.execute("CREATE TABLE patients (" +
                "id SERIAL PRIMARY KEY, " +
                "name VARCHAR(100) NOT NULL, " +
                "age INTEGER NOT NULL, " +
                "gender VARCHAR(10) NOT NULL, " +
                "address TEXT NOT NULL, " +
                "emergency_contact VARCHAR(50) NOT NULL, " +
                "unique_code VARCHAR(20) UNIQUE NOT NULL, " +
                "caregiver_id INTEGER REFERENCES users(id) ON DELETE SET NULL" +
                ")");
        
        stmt.execute("CREATE TABLE family_patient (" +
                "id SERIAL PRIMARY KEY, " +
                "family_id INTEGER REFERENCES users(id) ON DELETE CASCADE, " +
                "patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE, " +
                "full_name VARCHAR(100), " +
                "email VARCHAR(100), " +
                "contact VARCHAR(50), " +
                "relationship VARCHAR(50), " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                "UNIQUE(family_id, patient_id)" +
                ")");
        
        stmt.execute("CREATE TABLE caregivers (" +
                "id SERIAL PRIMARY KEY, " +
                "user_id INTEGER REFERENCES users(id) ON DELETE CASCADE, " +
                "specialization VARCHAR(100), " +
                "patient_ids INTEGER[], " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                ")");
        
        stmt.execute("CREATE TABLE health_data (" +
                "id SERIAL PRIMARY KEY, " +
                "patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE, " +
                "heart_rate DOUBLE PRECISION NOT NULL, " +
                "blood_pressure VARCHAR(20) NOT NULL, " +
                "temperature DOUBLE PRECISION NOT NULL, " +
                "position VARCHAR(50), " +
                "timestamp TIMESTAMP NOT NULL" +
                ")");
        
        stmt.execute("CREATE TABLE critical_parameters (" +
                "id SERIAL PRIMARY KEY, " +
                "patient_id INTEGER REFERENCES patients(id) ON DELETE CASCADE, " +
                "min_heart_rate DOUBLE PRECISION NOT NULL, " +
                "max_heart_rate DOUBLE PRECISION NOT NULL, " +
                "min_blood_pressure VARCHAR(20) NOT NULL, " +
                "max_blood_pressure VARCHAR(20) NOT NULL, " +
                "min_temperature DOUBLE PRECISION NOT NULL, " +
                "max_temperature DOUBLE PRECISION NOT NULL" +
                ")");
        
        stmt.close();
    }
    
    private void seedData() throws SQLException {
        seedPatients();
        seedCaregivers();
        seedFamilies();
        seedCriticalParameters();
        seedHealthHistory();
    }
    
    private void seedPatients() throws SQLException {
        String[] patientData = {
            "Ahmad Wijaya,75,Male,Jl. Merdeka No. 123 Jakarta,081234567890,P12345",
            "Dewi Sartika,68,Female,Jl. Sudirman No. 456 Bandung,081234567891,P67890",
            "Budi Santoso,72,Male,Jl. Thamrin No. 789 Surabaya,081234567892,P24680",
            "Siti Nurhaliza,70,Female,Jl. Gatot Subroto No. 321 Medan,081234567893,P13579",
            "Andi Pratama,65,Male,Jl. Diponegoro No. 654 Yogyakarta,081234567894,P97531",
            "Rina Kusuma,73,Female,Jl. Ahmad Yani No. 987 Semarang,081234567895,P86420",
            "Hendra Gunawan,69,Male,Jl. Veteran No. 147 Malang,081234567896,P75319",
            "Maya Sari,71,Female,Jl. Pahlawan No. 258 Denpasar,081234567897,P64208",
            "Rudi Hermawan,67,Male,Jl. Kartini No. 369 Palembang,081234567898,P53197",
            "Lina Marlina,74,Female,Jl. Cut Nyak Dien No. 741 Makassar,081234567899,P42086",
            "Joko Susilo,76,Male,Jl. Asia Afrika No. 111 Bandung,081234567811,P11111",
            "Siti Rahayu,77,Female,Jl. Pemuda No. 222 Surabaya,081234567822,P22222",
            "Bambang Sudrajat,78,Male,Jl. Gajah Mada No. 333 Jakarta,081234567833,P33333",
            "Ani Wijayanti,79,Female,Jl. Hayam Wuruk No. 444 Yogyakarta,081234567844,P44444",
            "Dian Purnama,80,Female,Jl. Pangeran Antasari No. 555 Banjarmasin,081234567855,P55555"
        };
        
        PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO patients (name, age, gender, address, emergency_contact, unique_code) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        
        for (String data : patientData) {
            String[] parts = data.split(",");
            pstmt.setString(1, parts[0]);
            pstmt.setInt(2, Integer.parseInt(parts[1]));
            pstmt.setString(3, parts[2]);
            pstmt.setString(4, parts[3]);
            pstmt.setString(5, parts[4]);
            pstmt.setString(6, parts[5]);
            pstmt.executeUpdate();
        }
        
        pstmt.close();
    }
    
    private void seedCaregivers() throws SQLException {
        String[] caregiverData = {
            "Dr. Sarah Johnson,sarah.johnson@caremonitor.com,password123,Geriatric Medicine,081111111111",
            "Dr. Michael Chen,michael.chen@caremonitor.com,password123,Cardiology,081111111112",
            "Dr. Lisa Anderson,lisa.anderson@caremonitor.com,password123,Internal Medicine,081111111113",
            "Dr. David Wilson,david.wilson@caremonitor.com,password123,Neurology,081111111114",
            "Dr. Emily Brown,emily.brown@caremonitor.com,password123,Endocrinology,081111111115",
            "Dr. James Taylor,james.taylor@caremonitor.com,password123,Pulmonology,081111111116",
            "Dr. Maria Garcia,maria.garcia@caremonitor.com,password123,General Practice,081111111117"
        };
        
        PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (full_name, email, password, role, specialization, contact) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        
        PreparedStatement caregiverTablePstmt = connection.prepareStatement(
                "INSERT INTO caregivers (user_id, specialization, patient_ids) VALUES (?, ?, ?)");
        
        for (int i = 0; i < caregiverData.length; i++) {
            String data = caregiverData[i];
            String[] parts = data.split(",");
            pstmt.setString(1, parts[0]);
            pstmt.setString(2, parts[1]);
            pstmt.setString(3, parts[2]);
            pstmt.setString(4, "CAREGIVER");
            pstmt.setString(5, parts[3]);
            pstmt.setString(6, parts[4]);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int userId = rs.getInt(1);
                
                caregiverTablePstmt.setInt(1, userId);
                caregiverTablePstmt.setString(2, parts[3]);
                
                Integer[] assignedPatients;
                switch (i) {
                    case 0: assignedPatients = new Integer[]{1, 2}; break;
                    case 1: assignedPatients = new Integer[]{3, 4}; break;
                    case 2: assignedPatients = new Integer[]{5}; break;
                    case 3: assignedPatients = new Integer[]{6, 7}; break;
                    case 4: assignedPatients = new Integer[]{8}; break;
                    case 5: assignedPatients = new Integer[]{9, 10}; break;
                    case 6: assignedPatients = new Integer[]{11}; break;
                    default: assignedPatients = new Integer[]{};
                }
                
                java.sql.Array sqlArray = connection.createArrayOf("INTEGER", assignedPatients);
                caregiverTablePstmt.setArray(3, sqlArray);
                caregiverTablePstmt.executeUpdate();
            }
        }
        
        pstmt.close();
        caregiverTablePstmt.close();
        
        assignPatientsToCaregiver(1, new int[]{1, 2});
        assignPatientsToCaregiver(2, new int[]{3, 4});
        assignPatientsToCaregiver(3, new int[]{5});
        assignPatientsToCaregiver(4, new int[]{6, 7});
        assignPatientsToCaregiver(5, new int[]{8});
        assignPatientsToCaregiver(6, new int[]{9, 10});
        assignPatientsToCaregiver(7, new int[]{11});
    }
    
    private void assignPatientsToCaregiver(int caregiverId, int[] patientIds) throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "UPDATE patients SET caregiver_id = ? WHERE id = ?");
        
        for (int patientId : patientIds) {
            pstmt.setInt(1, caregiverId);
            pstmt.setInt(2, patientId);
            pstmt.executeUpdate();
        }
        
        pstmt.close();
    }
    
    private void seedFamilies() throws SQLException {
        String[] familyData = {
            "Andi Wijaya,andi.wijaya@email.com,password123,081222222221,Son,1",
            "Sari Sartika,sari.sartika@email.com,password123,081222222222,Daughter,2",
            "Indra Santoso,indra.santoso@email.com,password123,081222222223,Son,3",
            "Rina Nurhaliza,rina.nurhaliza@email.com,password123,081222222224,Daughter,4",
            "Dedi Pratama,dedi.pratama@email.com,password123,081222222225,Son,5",
            "Sinta Kusuma,sinta.kusuma@email.com,password123,081222222226,Daughter,6",
            "Agus Gunawan,agus.gunawan@email.com,password123,081222222227,Son,7"
        };
        
        PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO users (full_name, email, password, role, contact, relationship) VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS);
        
        PreparedStatement familyPatientPstmt = connection.prepareStatement(
                "INSERT INTO family_patient (family_id, patient_id, full_name, email, contact, relationship) VALUES (?, ?, ?, ?, ?, ?)");
        
        for (String data : familyData) {
            String[] parts = data.split(",");
            pstmt.setString(1, parts[0]);
            pstmt.setString(2, parts[1]);
            pstmt.setString(3, parts[2]);
            pstmt.setString(4, "FAMILY");
            pstmt.setString(5, parts[3]);
            pstmt.setString(6, parts[4]);
            pstmt.executeUpdate();
            
            ResultSet rs = pstmt.getGeneratedKeys();
            if (rs.next()) {
                int familyId = rs.getInt(1);
                int patientId = Integer.parseInt(parts[5]);
                
                familyPatientPstmt.setInt(1, familyId);
                familyPatientPstmt.setInt(2, patientId);
                familyPatientPstmt.setString(3, parts[0]);
                familyPatientPstmt.setString(4, parts[1]);
                familyPatientPstmt.setString(5, parts[3]);
                familyPatientPstmt.setString(6, parts[4]);
                familyPatientPstmt.executeUpdate();
            }
        }
        
        pstmt.close();
        familyPatientPstmt.close();
    }
    
    private void seedCriticalParameters() throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO critical_parameters (patient_id, min_heart_rate, max_heart_rate, " +
                "min_blood_pressure, max_blood_pressure, min_temperature, max_temperature) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)");
        
        for (int i = 1; i <= 15; i++) {
            pstmt.setInt(1, i);
            pstmt.setDouble(2, 60.0);
            pstmt.setDouble(3, 100.0);
            pstmt.setString(4, "90/60");
            pstmt.setString(5, "120/80");
            pstmt.setDouble(6, 36.1);
            pstmt.setDouble(7, 37.2);
            pstmt.executeUpdate();
        }
        
        pstmt.close();
    }
    
    private void seedHealthHistory() throws SQLException {
        PreparedStatement pstmt = connection.prepareStatement(
                "INSERT INTO health_data (patient_id, heart_rate, blood_pressure, temperature, position, timestamp) " +
                "VALUES (?, ?, ?, ?, ?, ?)");
        
        Random random = new Random();
        LocalDateTime now = LocalDateTime.now();
        
        for (int patientId = 1; patientId <= 15; patientId++) {
            for (int day = 30; day >= 0; day--) {
                int recordsPerDay = 3 + random.nextInt(3);
                
                for (int record = 0; record < recordsPerDay; record++) {
                    LocalDateTime timestamp = now.minusDays(day).plusHours(random.nextInt(24));
                    
                    double heartRate = 65 + random.nextGaussian() * 8;
                    heartRate = Math.max(50, Math.min(120, heartRate));
                    
                    int systolic = 115 + (int)(random.nextGaussian() * 10);
                    systolic = Math.max(90, Math.min(150, systolic));
                    
                    int diastolic = 75 + (int)(random.nextGaussian() * 5);
                    diastolic = Math.max(60, Math.min(95, diastolic));
                    
                    double temperature = 36.8 + random.nextGaussian() * 0.3;
                    temperature = Math.max(35.5, Math.min(38.5, temperature));
                    
                    String[] positions = {"Lying", "Sitting", "Standing"};
                    String position = positions[random.nextInt(positions.length)];
                    
                    pstmt.setInt(1, patientId);
                    pstmt.setDouble(2, heartRate);
                    pstmt.setString(3, systolic + "/" + diastolic);
                    pstmt.setDouble(4, temperature);
                    pstmt.setString(5, position);
                    pstmt.setTimestamp(6, Timestamp.valueOf(timestamp));
                    pstmt.executeUpdate();
                }
            }
        }
        
        pstmt.close();
    }

    
    public User authenticateUser(String email, String password) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM users WHERE email = ? AND password = ?");
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("id");
                String fullName = rs.getString("full_name");
                String role = rs.getString("role");
                
                updateLastLogin(id);
                
                if ("CAREGIVER".equals(role)) {
                    String specialization = rs.getString("specialization");
                    String contact = rs.getString("contact");
                    
                    Caregiver caregiver = new Caregiver(id, fullName, email, password, specialization, contact);
                    loadCaregiverPatients(caregiver);
                    return caregiver;
                } else if ("FAMILY".equals(role)) {
                    String contact = rs.getString("contact");
                    String relationship = rs.getString("relationship");
                    
                    Family family = new Family(id, fullName, email, password, contact, relationship);
                    loadFamilyPatient(family);
                    return family;
                }
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    private void updateLastLogin(int userId) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE users SET last_login = ? WHERE id = ?");
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setInt(2, userId);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public boolean registerUser(User user, int patientId, String uniqueCode) {
        try {
            connection.setAutoCommit(false);
            
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO users (full_name, email, password, role, specialization, contact, relationship) VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getPassword());
            pstmt.setString(4, user.getRole());
            
            if (user instanceof Caregiver) {
                Caregiver caregiver = (Caregiver) user;
                pstmt.setString(5, caregiver.getSpecialization());
                pstmt.setString(6, caregiver.getContact());
                pstmt.setString(7, null);
            } else if (user instanceof Family) {
                Family family = (Family) user;
                pstmt.setString(5, null);
                pstmt.setString(6, family.getContact());
                pstmt.setString(7, family.getRelationship());
            } else {
                pstmt.setString(5, null);
                pstmt.setString(6, null);
                pstmt.setString(7, null);
            }
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows == 0) {
                connection.rollback();
                return false;
            }
            
            ResultSet generatedKeys = pstmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getInt(1));
            } else {
                connection.rollback();
                return false;
            }
            
            pstmt = connection.prepareStatement(
                    "SELECT * FROM patients WHERE id = ? AND unique_code = ?");
            pstmt.setInt(1, patientId);
            pstmt.setString(2, uniqueCode);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (!rs.next()) {
                connection.rollback();
                return false;
            }
            
            if ("CAREGIVER".equals(user.getRole())) {
                pstmt = connection.prepareStatement(
                        "SELECT caregiver_id FROM patients WHERE id = ?");
                pstmt.setInt(1, patientId);
                
                rs = pstmt.executeQuery();
                
                if (rs.next() && rs.getObject("caregiver_id") != null) {
                    connection.rollback();
                    return false;
                }
                
                pstmt = connection.prepareStatement(
                        "UPDATE patients SET caregiver_id = ? WHERE id = ?");
                pstmt.setInt(1, user.getId());
                pstmt.setInt(2, patientId);
                pstmt.executeUpdate();
            } else if ("FAMILY".equals(user.getRole())) {
                pstmt = connection.prepareStatement(
                        "INSERT INTO family_patient (family_id, patient_id) VALUES (?, ?)");
                pstmt.setInt(1, user.getId());
                pstmt.setInt(2, patientId);
                pstmt.executeUpdate();
            }
            
            connection.commit();
            pstmt.close();
            return true;
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            e.printStackTrace();
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadCaregiverPatients(Caregiver caregiver) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM patients WHERE caregiver_id = ?");
            pstmt.setInt(1, caregiver.getId());
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setAddress(rs.getString("address"));
                patient.setEmergencyContact(rs.getString("emergency_contact"));
                patient.setUniqueCode(rs.getString("unique_code"));
                patient.setCaregiverId(caregiver.getId());
                
                caregiver.addPatient(patient);
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    private void loadFamilyPatient(Family family) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT p.* FROM patients p " +
                    "JOIN family_patient fp ON p.id = fp.patient_id " +
                    "WHERE fp.family_id = ?");
            pstmt.setInt(1, family.getId());
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setAddress(rs.getString("address"));
                patient.setEmergencyContact(rs.getString("emergency_contact"));
                patient.setUniqueCode(rs.getString("unique_code"));
                
                if (rs.getObject("caregiver_id") != null) {
                    patient.setCaregiverId(rs.getInt("caregiver_id"));
                }
                
                family.setPatient(patient);
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        
        try {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM patients");
            
            while (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setAddress(rs.getString("address"));
                patient.setEmergencyContact(rs.getString("emergency_contact"));
                patient.setUniqueCode(rs.getString("unique_code"));
                
                if (rs.getObject("caregiver_id") != null) {
                    patient.setCaregiverId(rs.getInt("caregiver_id"));
                }
                
                patients.add(patient);
            }
            
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return patients;
    }
    
    public Patient getPatientById(int id) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM patients WHERE id = ?");
            pstmt.setInt(1, id);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                Patient patient = new Patient();
                patient.setId(rs.getInt("id"));
                patient.setName(rs.getString("name"));
                patient.setAge(rs.getInt("age"));
                patient.setGender(rs.getString("gender"));
                patient.setAddress(rs.getString("address"));
                patient.setEmergencyContact(rs.getString("emergency_contact"));
                patient.setUniqueCode(rs.getString("unique_code"));
                
                if (rs.getObject("caregiver_id") != null) {
                    patient.setCaregiverId(rs.getInt("caregiver_id"));
                }
                
                pstmt.close();
                return patient;
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }
    
    public boolean verifyPatientCode(int patientId, String uniqueCode) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM patients WHERE id = ? AND unique_code = ?");
            pstmt.setInt(1, patientId);
            pstmt.setString(2, uniqueCode);
            
            ResultSet rs = pstmt.executeQuery();
            boolean result = rs.next();
            
            pstmt.close();
            return result;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public boolean saveHealthData(HealthData data) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "INSERT INTO health_data (patient_id, heart_rate, blood_pressure, temperature, position, timestamp) " +
                    "VALUES (?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, data.getPatientId());
            pstmt.setDouble(2, data.getHeartRate());
            pstmt.setString(3, data.getBloodPressure());
            pstmt.setDouble(4, data.getTemperature());
            pstmt.setString(5, data.getPosition());
            pstmt.setTimestamp(6, Timestamp.valueOf(data.getTimestamp()));
            
            int affectedRows = pstmt.executeUpdate();
            
            if (affectedRows > 0) {
                ResultSet generatedKeys = pstmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    data.setId(generatedKeys.getInt(1));
                    pstmt.close();
                    return true;
                }
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return false;
    }
    
    public List<HealthData> getHealthDataByPatientId(int patientId) {
        List<HealthData> dataList = new ArrayList<>();
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM health_data WHERE patient_id = ? ORDER BY timestamp DESC LIMIT 100");
            pstmt.setInt(1, patientId);
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                HealthData data = new HealthData();
                data.setId(rs.getInt("id"));
                data.setPatientId(rs.getInt("patient_id"));
                data.setHeartRate(rs.getDouble("heart_rate"));
                data.setBloodPressure(rs.getString("blood_pressure"));
                data.setTemperature(rs.getDouble("temperature"));
                data.setPosition(rs.getString("position"));
                data.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                
                dataList.add(data);
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dataList;
    }
    
    public List<HealthData> getHealthDataByPatientIdAndDateRange(int patientId, LocalDateTime start, LocalDateTime end) {
        List<HealthData> dataList = new ArrayList<>();
        
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM health_data WHERE patient_id = ? AND timestamp BETWEEN ? AND ? ORDER BY timestamp");
            pstmt.setInt(1, patientId);
            pstmt.setTimestamp(2, Timestamp.valueOf(start));
            pstmt.setTimestamp(3, Timestamp.valueOf(end));
            
            ResultSet rs = pstmt.executeQuery();
            
            while (rs.next()) {
                HealthData data = new HealthData();
                data.setId(rs.getInt("id"));
                data.setPatientId(rs.getInt("patient_id"));
                data.setHeartRate(rs.getDouble("heart_rate"));
                data.setBloodPressure(rs.getString("blood_pressure"));
                data.setTemperature(rs.getDouble("temperature"));
                data.setPosition(rs.getString("position"));
                data.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                
                dataList.add(data);
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return dataList;
    }
    
    public CriticalParameter getCriticalParametersByPatientId(int patientId) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "SELECT * FROM critical_parameters WHERE patient_id = ?");
            pstmt.setInt(1, patientId);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                CriticalParameter params = new CriticalParameter();
                params.setId(rs.getInt("id"));
                params.setPatientId(rs.getInt("patient_id"));
                params.setMinHeartRate(rs.getDouble("min_heart_rate"));
                params.setMaxHeartRate(rs.getDouble("max_heart_rate"));
                params.setMinBloodPressure(rs.getString("min_blood_pressure"));
                params.setMaxBloodPressure(rs.getString("max_blood_pressure"));
                params.setMinTemperature(rs.getDouble("min_temperature"));
                params.setMaxTemperature(rs.getDouble("max_temperature"));
                
                pstmt.close();
                return params;
            }
            
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return new CriticalParameter(patientId);
    }
    
    public boolean updateCriticalParameters(CriticalParameter params) {
        try {
            PreparedStatement pstmt = connection.prepareStatement(
                    "UPDATE critical_parameters SET min_heart_rate = ?, max_heart_rate = ?, " +
                    "min_blood_pressure = ?, max_blood_pressure = ?, min_temperature = ?, max_temperature = ? " +
                    "WHERE patient_id = ?");
            pstmt.setDouble(1, params.getMinHeartRate());
            pstmt.setDouble(2, params.getMaxHeartRate());
            pstmt.setString(3, params.getMinBloodPressure());
            pstmt.setString(4, params.getMaxBloodPressure());
            pstmt.setDouble(5, params.getMinTemperature());
            pstmt.setDouble(6, params.getMaxTemperature());
            pstmt.setInt(7, params.getPatientId());
            
            int affectedRows = pstmt.executeUpdate();
            pstmt.close();
            
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
