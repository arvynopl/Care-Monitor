// FIle: src/main/java/com/caremonitor/controller/HealthDataController.java
package com.caremonitor.controller;

import com.caremonitor.model.DatabaseManager;
import com.caremonitor.model.HealthData;
import com.caremonitor.model.Patient;
// import com.caremonitor.model.CriticalParameter;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;

import java.io.FileOutputStream;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class HealthDataController {
    private DatabaseManager dbManager;
    
    public HealthDataController() {
        this.dbManager = DatabaseManager.getInstance();
    }
    
    public boolean saveHealthData(HealthData healthData) {
        String sql = "INSERT INTO health_data (patient_id, heart_rate, blood_pressure, temperature, position) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, healthData.getPatientId());
            pstmt.setDouble(2, healthData.getHeartRate()); 
            pstmt.setString(3, healthData.getBloodPressure());
            pstmt.setDouble(4, healthData.getTemperature());
            pstmt.setString(5, healthData.getPosition());
            
            int result = pstmt.executeUpdate();
            return result > 0;
        } catch (SQLException e) {
            System.err.println("Error saving health data: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public HealthData getLatestHealthData(int patientId) {
        String sql = "SELECT * FROM health_data WHERE patient_id = ? ORDER BY timestamp DESC LIMIT 1";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    HealthData healthData = new HealthData();
                    healthData.setId(rs.getInt("id"));
                    healthData.setPatientId(rs.getInt("patient_id"));
                    healthData.setHeartRate(rs.getDouble("heart_rate"));
                    healthData.setBloodPressure(rs.getString("blood_pressure"));
                    healthData.setTemperature(rs.getDouble("temperature"));
                    healthData.setPosition(rs.getString("position"));
                    healthData.setTimestamp(rs.getTimestamp("timestamp")); 
                    return healthData;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting latest health data: " + e.getMessage());
            e.printStackTrace();
        }
        
        return null;
    }
    
    public List<HealthData> getHealthDataByPatientId(int patientId) {
        List<HealthData> healthDataList = new ArrayList<>();
        String sql = "SELECT * FROM health_data WHERE patient_id = ? ORDER BY timestamp DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HealthData healthData = new HealthData();
                    healthData.setId(rs.getInt("id"));
                    healthData.setPatientId(rs.getInt("patient_id"));
                    healthData.setHeartRate(rs.getDouble("heart_rate"));
                    healthData.setBloodPressure(rs.getString("blood_pressure"));
                    healthData.setTemperature(rs.getDouble("temperature"));
                    healthData.setPosition(rs.getString("position"));
                    healthData.setTimestamp(rs.getTimestamp("timestamp")); 
                    healthDataList.add(healthData);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting health data by patient ID: " + e.getMessage());
            e.printStackTrace();
        }
        
        return healthDataList;
    }
    
    public List<HealthData> getHealthDataByPatientIdAndDateRange(int patientId, LocalDateTime fromDate, LocalDateTime toDate) {
        List<HealthData> healthDataList = new ArrayList<>();
        String sql = "SELECT * FROM health_data WHERE patient_id = ? AND timestamp BETWEEN ? AND ? ORDER BY timestamp DESC";
        
        try (Connection conn = dbManager.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, patientId);
            pstmt.setTimestamp(2, Timestamp.valueOf(fromDate));
            pstmt.setTimestamp(3, Timestamp.valueOf(toDate));
            
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    HealthData healthData = new HealthData();
                    healthData.setId(rs.getInt("id"));
                    healthData.setPatientId(rs.getInt("patient_id"));
                    healthData.setHeartRate(rs.getDouble("heart_rate"));
                    healthData.setBloodPressure(rs.getString("blood_pressure"));
                    healthData.setTemperature(rs.getDouble("temperature"));
                    healthData.setPosition(rs.getString("position"));
                    healthData.setTimestamp(rs.getTimestamp("timestamp")); 
                    healthDataList.add(healthData);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error getting health data by date range: " + e.getMessage());
            e.printStackTrace();
        }
        
        return healthDataList;
    }
    
    public boolean isHealthDataCritical(HealthData healthData) {
        if (healthData.getHeartRate() < 60 || healthData.getHeartRate() > 100) {
            return true;
        }
        
        if (healthData.getTemperature() < 36.1 || healthData.getTemperature() > 37.2) {
            return true;
        }
        
        String[] bpParts = healthData.getBloodPressure().split("/");
        if (bpParts.length == 2) {
            try {
                int systolic = Integer.parseInt(bpParts[0]);
                int diastolic = Integer.parseInt(bpParts[1]);
                if (systolic < 90 || systolic > 140 || diastolic < 60 || diastolic > 90) {
                    return true;
                }
            } catch (NumberFormatException e) {
                System.err.println("Invalid blood pressure format: " + healthData.getBloodPressure());
            }
        }
        
        return false;
    }
    
    public List<String> getAbnormalParameters(HealthData healthData) {
        List<String> abnormalParams = new ArrayList<>();
        
        if (healthData.getHeartRate() < 60 || healthData.getHeartRate() > 100) {
            abnormalParams.add("Heart Rate: " + healthData.getHeartRate() + " bpm (Normal: 60-100)");
        }
        
        if (healthData.getTemperature() < 36.1 || healthData.getTemperature() > 37.2) {
            abnormalParams.add("Temperature: " + healthData.getTemperature() + "°C (Normal: 36.1-37.2)");
        }
        
        String[] bpParts = healthData.getBloodPressure().split("/");
        if (bpParts.length == 2) {
            try {
                int systolic = Integer.parseInt(bpParts[0]);
                int diastolic = Integer.parseInt(bpParts[1]);
                
                if (systolic < 90 || systolic > 140 || diastolic < 60 || diastolic > 90) {
                    abnormalParams.add("Blood Pressure: " + healthData.getBloodPressure() + " (Normal: 90-140/60-90)");
                }
            } catch (NumberFormatException e) {
                abnormalParams.add("Blood Pressure: Invalid format - " + healthData.getBloodPressure());
            }
        }
        
        return abnormalParams;
    }

    public void generatePDFReport(String filePath, Patient patient,
                                  LocalDateTime fromDate, LocalDateTime toDate) throws Exception {
        List<HealthData> healthDataList =
            getHealthDataByPatientIdAndDateRange(patient.getId(), fromDate, toDate);

        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            Document document = new Document();
            PdfWriter.getInstance(document, fos);
            try {
                document.open();

                Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
                Paragraph title = new Paragraph("Health Report - " + patient.getName(), titleFont);
                title.setAlignment(Element.ALIGN_CENTER);
                document.add(title);
                document.add(new Paragraph(" "));

                Font headerFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12);
                document.add(new Paragraph("Patient Information:", headerFont));
                document.add(new Paragraph("Name: " + patient.getName()));
                document.add(new Paragraph("Age: " + patient.getAge()));
                document.add(new Paragraph("Gender: " + patient.getGender()));
                document.add(new Paragraph("Address: " + patient.getAddress()));
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Report Period: " + fromDate + " to " + toDate, headerFont));
                document.add(new Paragraph(" "));

                document.add(new Paragraph("Health Data Records:", headerFont));

                PdfPTable table = new PdfPTable(5);
                table.setWidthPercentage(100);

                table.addCell("Date & Time");
                table.addCell("Heart Rate");
                table.addCell("Temperature");
                table.addCell("Blood Pressure");
                table.addCell("Position");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
                for (HealthData data : healthDataList) {
                    table.addCell(data.getTimestamp().format(formatter));
                    table.addCell(String.valueOf(data.getHeartRate()));
                    table.addCell(String.format("%.1f", data.getTemperature()));
                    table.addCell(data.getBloodPressure());
                    table.addCell(data.getPosition());
                }

                document.add(table);
            } finally {
                document.close();
            }
        }
    }
}
