// File: src/main/java/com/caremonitor/controller/CriticalParameterController.java
package com.caremonitor.controller;

import com.caremonitor.model.DatabaseManager;
import com.caremonitor.model.CriticalParameter;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CriticalParameterController {
    private DatabaseManager dbManager;
    
    public CriticalParameterController() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public CriticalParameter getCriticalParametersByPatientId(int patientId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        
        try {
            conn = dbManager.getConnection();
            
            String query = "SELECT * FROM critical_parameters WHERE patient_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            
            rs = stmt.executeQuery();
            
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
                
                return params;
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving critical parameters: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        
        return new CriticalParameter(patientId);
    }
    
    public boolean updateCriticalParameters(CriticalParameter params) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = dbManager.getConnection();
            
            String checkQuery = "SELECT id FROM critical_parameters WHERE patient_id = ?";
            stmt = conn.prepareStatement(checkQuery);
            stmt.setInt(1, params.getPatientId());
            
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                String updateQuery = "UPDATE critical_parameters SET " +
                                   "min_heart_rate = ?, max_heart_rate = ?, " +
                                   "min_blood_pressure = ?, max_blood_pressure = ?, " +
                                   "min_temperature = ?, max_temperature = ? " +
                                   "WHERE patient_id = ?";
                
                stmt = conn.prepareStatement(updateQuery);
                stmt.setDouble(1, params.getMinHeartRate());
                stmt.setDouble(2, params.getMaxHeartRate());
                stmt.setString(3, params.getMinBloodPressure());
                stmt.setString(4, params.getMaxBloodPressure());
                stmt.setDouble(5, params.getMinTemperature());
                stmt.setDouble(6, params.getMaxTemperature());
                stmt.setInt(7, params.getPatientId());
                
                int rowsAffected = stmt.executeUpdate();
                success = rowsAffected > 0;
                
                System.out.println("Updated critical parameters for patient ID: " + params.getPatientId());
            } else {
                String insertQuery = "INSERT INTO critical_parameters " +
                                   "(patient_id, min_heart_rate, max_heart_rate, " +
                                   "min_blood_pressure, max_blood_pressure, " +
                                   "min_temperature, max_temperature) " +
                                   "VALUES (?, ?, ?, ?, ?, ?, ?)";
                
                stmt = conn.prepareStatement(insertQuery);
                stmt.setInt(1, params.getPatientId());
                stmt.setDouble(2, params.getMinHeartRate());
                stmt.setDouble(3, params.getMaxHeartRate());
                stmt.setString(4, params.getMinBloodPressure());
                stmt.setString(5, params.getMaxBloodPressure());
                stmt.setDouble(6, params.getMinTemperature());
                stmt.setDouble(7, params.getMaxTemperature());
                
                int rowsAffected = stmt.executeUpdate();
                success = rowsAffected > 0;
                
                System.out.println("Inserted new critical parameters for patient ID: " + params.getPatientId());
            }
            
        } catch (SQLException e) {
            System.err.println("Error updating critical parameters: " + e.getMessage());
            e.printStackTrace();
            success = false;
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        
        return success;
    }
    
    public boolean saveCriticalParameter(int patientId, double heartRate, String bloodPressure, double temperature) {
        CriticalParameter params = new CriticalParameter(patientId);
        params.setMinHeartRate(60.0); 
        params.setMaxHeartRate(heartRate);
        params.setMinBloodPressure("90/60"); 
        params.setMaxBloodPressure(bloodPressure);
        params.setMinTemperature(36.0);
        params.setMaxTemperature(temperature);
        
        return updateCriticalParameters(params);
    }
    
    public boolean deleteCriticalParameters(int patientId) {
        Connection conn = null;
        PreparedStatement stmt = null;
        boolean success = false;
        
        try {
            conn = dbManager.getConnection();
            
            String query = "DELETE FROM critical_parameters WHERE patient_id = ?";
            stmt = conn.prepareStatement(query);
            stmt.setInt(1, patientId);
            
            int rowsAffected = stmt.executeUpdate();
            success = rowsAffected > 0;
            
        } catch (SQLException e) {
            System.err.println("Error deleting critical parameters: " + e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.err.println("Error closing database resources: " + e.getMessage());
            }
        }
        
        return success;
    }
}