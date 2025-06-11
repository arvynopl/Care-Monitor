//File: src/main/java/com/caremonitor/controller/PatientController.java
package com.caremonitor.controller;

import com.caremonitor.model.DatabaseManager;
import com.caremonitor.model.Patient;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PatientController {
    private DatabaseManager dbManager;

    public PatientController() {
        this.dbManager = DatabaseManager.getInstance();
    }

    public List<Patient> getPatientsByCaregiver(int caregiverId) {
        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT * FROM patients WHERE caregiver_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, caregiverId);
            System.out.println("Executing query for caregiver ID: " + caregiverId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("emergency_contact"),
                        rs.getString("unique_code")
                    );
                    patients.add(patient);
                    System.out.println("Found patient: " + patient.getName() + " (ID: " + patient.getId() + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting patients by caregiver: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Total patients found for caregiver " + caregiverId + ": " + patients.size());
        return patients;
    }

    public Patient getPatientByFamily(int familyId) {
        String sql = "SELECT p.* FROM patients p " +
                     "INNER JOIN family_patient fp ON p.id = fp.patient_id " +
                     "WHERE fp.family_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, familyId);
            System.out.println("Executing query for family ID: " + familyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("emergency_contact"),
                        rs.getString("unique_code")
                    );
                    System.out.println("Found patient for family: " + patient.getName() + " (ID: " + patient.getId() + ")");
                    return patient;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting patient by family: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("No patient found for family ID: " + familyId);
        return null;
    }

    public List<Patient> getPatientsByFamily(int familyId) {
        List<Patient> patients = new ArrayList<>();

        String sql = "SELECT p.* FROM patients p " +
                     "INNER JOIN family_patient fp ON p.id = fp.patient_id " +
                     "WHERE fp.family_id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, familyId);
            System.out.println("Executing query for family ID: " + familyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("emergency_contact"),
                        rs.getString("unique_code")
                    );
                    patients.add(patient);
                    System.out.println("Found patient for family: " + patient.getName() + " (ID: " + patient.getId() + ")");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting patients by family: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("Total patients found for family " + familyId + ": " + patients.size());
        return patients;
    }
    
    public List<Patient> getUnassignedPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients WHERE caregiver_id IS NULL";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("emergency_contact"),
                        rs.getString("unique_code")
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving unassigned patients: " + e.getMessage());
            e.printStackTrace();
        }
        return patients;
    }

    public Patient getPatientById(int patientId) {
        String sql = "SELECT * FROM patients WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("emergency_contact"),
                        rs.getString("unique_code")
                    );
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving patient by ID: " + e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public List<Patient> getAllPatients() {
        List<Patient> patients = new ArrayList<>();
        String sql = "SELECT * FROM patients";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Patient patient = new Patient(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getInt("age"),
                        rs.getString("gender"),
                        rs.getString("address"),
                        rs.getString("emergency_contact"),
                        rs.getString("unique_code")
                    );
                    patients.add(patient);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all patients: " + e.getMessage());
            e.printStackTrace();
        }

        return patients;
    }

    public boolean addPatient(Patient patient) {
        String sql = "INSERT INTO patients (name, age, gender, address, emergency_contact, unique_code, caregiver_id) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getGender());
            pstmt.setString(4, patient.getAddress());
            pstmt.setString(5, patient.getEmergencyContact());
            pstmt.setString(6, patient.getUniqueCode());
            pstmt.setInt(7, patient.getCaregiverId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error adding patient: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePatient(Patient patient) {
        String sql = "UPDATE patients SET name = ?, age = ?, gender = ?, address = ?, " +
                     "emergency_contact = ?, unique_code = ?, caregiver_id = ? " +
                     "WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, patient.getName());
            pstmt.setInt(2, patient.getAge());
            pstmt.setString(3, patient.getGender());
            pstmt.setString(4, patient.getAddress());
            pstmt.setString(5, patient.getEmergencyContact());
            pstmt.setString(6, patient.getUniqueCode());
            pstmt.setInt(7, patient.getCaregiverId());
            pstmt.setInt(8, patient.getId());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error updating patient: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePatient(int patientId) {
        String sql = "DELETE FROM patients WHERE id = ?";

        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, patientId);

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error deleting patient: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}