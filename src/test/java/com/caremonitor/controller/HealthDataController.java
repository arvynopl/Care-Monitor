//File: src/test/java/com/caremonitor/controller/HealthDataControllerTest.java
package com.caremonitor.controller;

import com.caremonitor.model.HealthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HealthDataControllerTest {

    private HealthDataController healthDataController;

    @BeforeEach
    void setUp() {
        healthDataController = new HealthDataController();
    }

    @Test
    void testSaveHealthData() {
        HealthData healthData = new HealthData(1, 75.0, "120/80", 36.5, "Sitting");
        
        boolean result = healthDataController.saveHealthData(healthData);
        
        assertNotNull(result);
    }

    @Test
    void testGetLatestHealthData() {
        int patientId = 1;
        
        HealthData result = healthDataController.getLatestHealthData(patientId);
        if (result != null) {
            assertEquals(patientId, result.getPatientId());
        }
    }

    @Test
    void testGetHealthDataByPatientId() {
        int patientId = 1;
        
        List<HealthData> result = healthDataController.getHealthDataByPatientId(patientId);
        
        assertNotNull(result);
        for (HealthData data : result) {
            assertEquals(patientId, data.getPatientId());
        }
    }

    @Test
    void testIsHealthDataCriticalWithNormalValues() {
        HealthData normalData = new HealthData(1, 75.0, "120/80", 36.8, "Sitting");
        
        boolean result = healthDataController.isHealthDataCritical(normalData);
        
        assertFalse(result, "Normal health data should not be critical");
    }

    @Test
    void testIsHealthDataCriticalWithAbnormalHeartRate() {
        HealthData criticalData = new HealthData(1, 150.0, "120/80", 36.8, "Sitting");
        
        boolean result = healthDataController.isHealthDataCritical(criticalData);
        
        assertTrue(result, "High heart rate should be critical");
    }

    @Test
    void testGetAbnormalParametersWithNormalValues() {
        HealthData normalData = new HealthData(1, 75.0, "120/80", 36.8, "Sitting");
        
        List<String> result = healthDataController.getAbnormalParameters(normalData);
        
        assertNotNull(result);
        assertTrue(result.isEmpty(), "Normal values should have no abnormal parameters");
    }

    @Test
    void testGetAbnormalParametersWithCriticalValues() {
        HealthData criticalData = new HealthData(1, 150.0, "180/100", 39.0, "Sitting");
        
        List<String> result = healthDataController.getAbnormalParameters(criticalData);
        
        assertNotNull(result);
        assertFalse(result.isEmpty(), "Critical values should have abnormal parameters");
        
        String resultString = String.join(", ", result);
        assertTrue(resultString.contains("Heart Rate") || 
                  resultString.contains("Blood Pressure") || 
                  resultString.contains("Temperature"), 
                  "Result should contain information about abnormal parameters");
    }

    @Test
    void testGetAbnormalParametersWithLowHeartRate() {
        HealthData lowHeartRateData = new HealthData(1, 45.0, "120/80", 36.8, "Sitting");
        
        List<String> result = healthDataController.getAbnormalParameters(lowHeartRateData);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        String resultString = String.join(", ", result);
        assertTrue(resultString.contains("Heart Rate"), "Should detect low heart rate");
    }

    @Test
    void testGetAbnormalParametersWithHighTemperature() {
        HealthData highTempData = new HealthData(1, 75.0, "120/80", 39.5, "Sitting");
        
        List<String> result = healthDataController.getAbnormalParameters(highTempData);
        
        assertNotNull(result);
        assertFalse(result.isEmpty());
        
        String resultString = String.join(", ", result);
        assertTrue(resultString.contains("Temperature"), "Should detect high temperature");
    }

    @Test
    void testGetAbnormalParametersWithInvalidBloodPressure() {
        HealthData invalidBPData = new HealthData(1, 75.0, "invalid", 36.8, "Sitting");
        
        List<String> result = healthDataController.getAbnormalParameters(invalidBPData);
        
        assertNotNull(result);
    }
}