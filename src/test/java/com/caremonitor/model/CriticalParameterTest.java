// File: src/test/java/com/caremonitor/model/CriticalParameterTest.java
package com.caremonitor.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class CriticalParameterTest {

    private CriticalParameter criticalParameter;

    @BeforeEach
    void setUp() {
        criticalParameter = new CriticalParameter();
    }

    @Test
    void testCriticalParameterConstructorWithPatientId() {
        int patientId = 1;
        CriticalParameter paramCriticalParameter = new CriticalParameter(patientId);

        assertEquals(patientId, paramCriticalParameter.getPatientId());
        assertEquals(60.0, paramCriticalParameter.getMinHeartRate());
        assertEquals(100.0, paramCriticalParameter.getMaxHeartRate());
        assertEquals("90/60", paramCriticalParameter.getMinBloodPressure());
        assertEquals("120/80", paramCriticalParameter.getMaxBloodPressure());
        assertEquals(36.1, paramCriticalParameter.getMinTemperature());
        assertEquals(37.2, paramCriticalParameter.getMaxTemperature());
        assertNotNull(paramCriticalParameter.getTimestamp());
    }

    @Test
    void testSettersAndGetters() {
        int id = 1;
        int patientId = 2;
        double minHeartRate = 50.0;
        double maxHeartRate = 110.0;
        String minBloodPressure = "80/50";
        String maxBloodPressure = "140/90";
        double minTemperature = 35.5;
        double maxTemperature = 38.0;
        String parameterType = "Heart Rate";
        String value = "150";
        String severity = "HIGH";
        LocalDateTime timestamp = LocalDateTime.now();

        criticalParameter.setId(id);
        criticalParameter.setPatientId(patientId);
        criticalParameter.setMinHeartRate(minHeartRate);
        criticalParameter.setMaxHeartRate(maxHeartRate);
        criticalParameter.setMinBloodPressure(minBloodPressure);
        criticalParameter.setMaxBloodPressure(maxBloodPressure);
        criticalParameter.setMinTemperature(minTemperature);
        criticalParameter.setMaxTemperature(maxTemperature);
        criticalParameter.setParameterType(parameterType);
        criticalParameter.setValue(value);
        criticalParameter.setSeverity(severity);
        criticalParameter.setTimestamp(timestamp);

        assertEquals(id, criticalParameter.getId());
        assertEquals(patientId, criticalParameter.getPatientId());
        assertEquals(minHeartRate, criticalParameter.getMinHeartRate());
        assertEquals(maxHeartRate, criticalParameter.getMaxHeartRate());
        assertEquals(minBloodPressure, criticalParameter.getMinBloodPressure());
        assertEquals(maxBloodPressure, criticalParameter.getMaxBloodPressure());
        assertEquals(minTemperature, criticalParameter.getMinTemperature());
        assertEquals(maxTemperature, criticalParameter.getMaxTemperature());
        assertEquals(parameterType, criticalParameter.getParameterType());
        assertEquals(value, criticalParameter.getValue());
        assertEquals(severity, criticalParameter.getSeverity());
        assertEquals(timestamp, criticalParameter.getTimestamp());
    }

    @Test
    void testIsHeartRateNormal() {
        CriticalParameter params = new CriticalParameter(1);

        assertTrue(params.isHeartRateNormal(75.0));
        assertTrue(params.isHeartRateNormal(60.0));
        assertTrue(params.isHeartRateNormal(100.0));
        assertFalse(params.isHeartRateNormal(59.0));
        assertFalse(params.isHeartRateNormal(101.0));
    }

    @Test
    void testIsBloodPressureNormal() {
        CriticalParameter params = new CriticalParameter(1);

        assertTrue(params.isBloodPressureNormal("100/70"));
        assertTrue(params.isBloodPressureNormal("90/60"));
        assertTrue(params.isBloodPressureNormal("120/80"));
        assertFalse(params.isBloodPressureNormal("89/59"));
        assertFalse(params.isBloodPressureNormal("121/81"));
        assertFalse(params.isBloodPressureNormal("invalid"));
        assertFalse(params.isBloodPressureNormal("120"));
    }

    @Test
    void testIsTemperatureNormal() {
        CriticalParameter params = new CriticalParameter(1);

        assertTrue(params.isTemperatureNormal(36.5));
        assertTrue(params.isTemperatureNormal(36.1));
        assertTrue(params.isTemperatureNormal(37.2));
        assertFalse(params.isTemperatureNormal(36.0));
        assertFalse(params.isTemperatureNormal(37.3));
    }

    @Test
    void testDefaultConstructorSetsTimestamp() {
        LocalDateTime before = LocalDateTime.now().minusSeconds(1);
        CriticalParameter newCriticalParameter = new CriticalParameter();
        LocalDateTime after = LocalDateTime.now().plusSeconds(1);

        assertNotNull(newCriticalParameter.getTimestamp());
        assertTrue(newCriticalParameter.getTimestamp().isAfter(before));
        assertTrue(newCriticalParameter.getTimestamp().isBefore(after));
    }
}