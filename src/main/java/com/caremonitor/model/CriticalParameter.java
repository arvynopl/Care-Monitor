// File: src/main/java/com/caremonitor/model/CriticalParameter.java
package com.caremonitor.model;

import java.time.LocalDateTime;

public class CriticalParameter {
    private int id;
    private int patientId;
    private double minHeartRate;
    private double maxHeartRate;
    private String minBloodPressure;
    private String maxBloodPressure;
    private double minTemperature;
    private double maxTemperature;
    
    private String parameterType;
    private String value;
    private String severity;
    private LocalDateTime timestamp;

    public CriticalParameter() {
        this.timestamp = LocalDateTime.now();
    }

    public CriticalParameter(int patientId) {
        this.patientId = patientId;
        this.timestamp = LocalDateTime.now();
        this.minHeartRate = 60.0;
        this.maxHeartRate = 100.0;
        this.minBloodPressure = "90/60";
        this.maxBloodPressure = "120/80";
        this.minTemperature = 36.1;
        this.maxTemperature = 37.2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public double getMinHeartRate() {
        return minHeartRate;
    }

    public void setMinHeartRate(double minHeartRate) {
        this.minHeartRate = minHeartRate;
    }

    public double getMaxHeartRate() {
        return maxHeartRate;
    }

    public void setMaxHeartRate(double maxHeartRate) {
        this.maxHeartRate = maxHeartRate;
    }
    
    public double getHeartRate() {
        return maxHeartRate; 
    }

    public String getMinBloodPressure() {
        return minBloodPressure;
    }

    public void setMinBloodPressure(String minBloodPressure) {
        this.minBloodPressure = minBloodPressure;
    }

    public String getMaxBloodPressure() {
        return maxBloodPressure;
    }

    public void setMaxBloodPressure(String maxBloodPressure) {
        this.maxBloodPressure = maxBloodPressure;
    }
    
    public String getBloodPressure() {
        return maxBloodPressure; 
    }

    public double getMinTemperature() {
        return minTemperature;
    }

    public void setMinTemperature(double minTemperature) {
        this.minTemperature = minTemperature;
    }

    public double getMaxTemperature() {
        return maxTemperature;
    }

    public void setMaxTemperature(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }
    
    public double getTemperature() {
        return maxTemperature; 
    }
    
    public String getParameterType() {
        return parameterType;
    }
    
    public void setParameterType(String parameterType) {
        this.parameterType = parameterType;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setValue(String value) {
        this.value = value;
    }
    
    public String getSeverity() {
        return severity;
    }
    
    public void setSeverity(String severity) {
        this.severity = severity;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isHeartRateNormal(double heartRate) {
        return heartRate >= minHeartRate && heartRate <= maxHeartRate;
    }

    public boolean isBloodPressureNormal(String bloodPressure) {
        try {
            String[] parts = bloodPressure.split("/");
            String[] minParts = minBloodPressure.split("/");
            String[] maxParts = maxBloodPressure.split("/");
            
            int systolic = Integer.parseInt(parts[0]);
            int diastolic = Integer.parseInt(parts[1]);
            
            int minSystolic = Integer.parseInt(minParts[0]);
            int minDiastolic = Integer.parseInt(minParts[1]);
            
            int maxSystolic = Integer.parseInt(maxParts[0]);
            int maxDiastolic = Integer.parseInt(maxParts[1]);
            
            return systolic >= minSystolic && systolic <= maxSystolic &&
                   diastolic >= minDiastolic && diastolic <= maxDiastolic;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTemperatureNormal(double temperature) {
        return temperature >= minTemperature && temperature <= maxTemperature;
    }
}