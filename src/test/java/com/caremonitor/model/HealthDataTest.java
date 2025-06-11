// File: src/test/java/com/caremonitor/model/HealthDataTest.java
package com.caremonitor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
class HealthDataTest {

    private HealthData healthData;

    @BeforeEach
    void setUp() {
        healthData = new HealthData();
    }

    @Test
    void testHealthDataConstructorWithParameters() {
        int patientId = 1;
        double heartRate = 75.0;
        String bloodPressure = "120/80";
        double temperature = 36.5;
        String position = "Sitting";

        HealthData paramHealthData = new HealthData(patientId, heartRate, bloodPressure, temperature, position);

        assertEquals(patientId, paramHealthData.getPatientId());
        assertEquals(heartRate, paramHealthData.getHeartRate());
        assertEquals(bloodPressure, paramHealthData.getBloodPressure());
        assertEquals(temperature, paramHealthData.getTemperature());
        assertEquals(position, paramHealthData.getPosition());
        assertNotNull(paramHealthData.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        int id = 1;
        int patientId = 2;
        double heartRate = 80.0;
        String bloodPressure = "130/85";
        double temperature = 37.0;
        String position = "Standing";
        LocalDateTime timestamp = LocalDateTime.now();

        healthData.setId(id);
        healthData.setPatientId(patientId);
        healthData.setHeartRate(heartRate);
        healthData.setBloodPressure(bloodPressure);
        healthData.setTemperature(temperature);
        healthData.setPosition(position);
        healthData.setTimestamp(timestamp);

        assertEquals(id, healthData.getId());
        assertEquals(patientId, healthData.getPatientId());
        assertEquals(heartRate, healthData.getHeartRate());
        assertEquals(bloodPressure, healthData.getBloodPressure());
        assertEquals(temperature, healthData.getTemperature());
        assertEquals(position, healthData.getPosition());
        assertEquals(timestamp, healthData.getTimestamp());
    }

    @Test
    void testDefaultConstructorSetsTimestamp() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        HealthData newHealthData = new HealthData();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertNotNull(newHealthData.getTimestamp());
        assertTrue(newHealthData.getTimestamp().isAfter(before));
        assertTrue(newHealthData.getTimestamp().isBefore(after));
    }

    @Test
    void testHeartRateIntCompatibility() {
        // Test setting heart rate as int
        healthData.setHeartRate(75);
        assertEquals(75.0, healthData.getHeartRate());
        assertEquals(75, healthData.getHeartRateAsInt());

        // Test setting heart rate as double
        healthData.setHeartRate(75.5);
        assertEquals(75.5, healthData.getHeartRate());
        assertEquals(76, healthData.getHeartRateAsInt()); // Should round to nearest int
    }

    @Test
    void testTimestampCompatibility() {
        // Test setting timestamp as LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.now();
        healthData.setTimestamp(localDateTime);
        assertEquals(localDateTime, healthData.getTimestamp());

        // Test setting timestamp as SQL Timestamp
        java.sql.Timestamp sqlTimestamp = java.sql.Timestamp.valueOf(localDateTime);
        healthData.setTimestamp(sqlTimestamp);
        assertEquals(localDateTime, healthData.getTimestamp());

        // Test setting null timestamp
        healthData.setTimestamp((java.sql.Timestamp) null);
        assertNotNull(healthData.getTimestamp()); // Should set to current time
    }
}