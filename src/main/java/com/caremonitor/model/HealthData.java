// File: src/main/java/com/caremonitor/model/HealthData.java
package com.caremonitor.model;

import java.time.LocalDateTime;
import java.sql.Timestamp;

public class HealthData {
    private int id;
    private int patientId;
    private double heartRate;
    private String bloodPressure;
    private double temperature;
    private String position;
    private LocalDateTime timestamp;

    public HealthData() {
        this.timestamp = LocalDateTime.now();
    }

    public HealthData(int patientId, double heartRate, String bloodPressure, double temperature, String position) {
        this.patientId = patientId;
        this.heartRate = heartRate;
        this.bloodPressure = bloodPressure;
        this.temperature = temperature;
        this.position = position;
        this.timestamp = LocalDateTime.now();
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

    public double getHeartRate() {
        return heartRate;
    }
    
    public int getHeartRateAsInt() {
        return (int) Math.round(heartRate);
    }

    public void setHeartRate(double heartRate) {
        this.heartRate = heartRate;
    }
    
    public void setHeartRate(int heartRate) {
        this.heartRate = (double) heartRate;
    }

    public String getBloodPressure() {
        return bloodPressure;
    }

    public void setBloodPressure(String bloodPressure) {
        this.bloodPressure = bloodPressure;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp != null ? timestamp.toLocalDateTime() : LocalDateTime.now();
    }
}