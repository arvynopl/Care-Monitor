// / File: src/main/java/com/caremonitor/simulation/IoTDeviceSimulator.java
package com.caremonitor.simulation;

import com.caremonitor.model.HealthData;
import com.caremonitor.model.Patient;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class IoTDeviceSimulator {
    private final List<Patient> patients;
    private final Map<Integer, HealthData> latestData;
    private final List<DataListener> listeners;
    private final Random random;
    private ScheduledExecutorService scheduler;
    private boolean isRunning;
    
    private final Map<Integer, LocalDateTime> lastCriticalAlert;
    private final Map<Integer, Integer> consecutiveNormalReadings;
    private static final int CRITICAL_ALERT_COOLDOWN_MINUTES = 15; 
    private static final int NORMAL_READINGS_BEFORE_NEXT_ALERT = 5; 
    public interface DataListener {
        void onDataReceived(int patientId, HealthData data);
        default void onCriticalAlert(int patientId, HealthData data, String alertMessage) {}
    }
    
    public IoTDeviceSimulator(List<Patient> patients) {
        this.patients = new ArrayList<>(patients);
        this.latestData = new ConcurrentHashMap<>();
        this.listeners = new ArrayList<>();
        this.random = new Random();
        this.lastCriticalAlert = new ConcurrentHashMap<>();
        this.consecutiveNormalReadings = new ConcurrentHashMap<>();
        this.isRunning = false;
        
        
        generateInitialData();
    }
    
    private void generateInitialData() {
        for (Patient patient : patients) {
            HealthData data = generateHealthData(patient.getId());
            latestData.put(patient.getId(), data);
        }
    }
    
    public void start() {
        if (isRunning) return;
        
        isRunning = true;
        scheduler = Executors.newScheduledThreadPool(2);
        
        scheduler.scheduleWithFixedDelay(this::generateDataForAllPatients, 5, 15, TimeUnit.SECONDS);
    }
    
    public void stop() {
        if (!isRunning) return;
        
        isRunning = false;
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }
    
    private void generateDataForAllPatients() {
        for (Patient patient : patients) {
            if (random.nextDouble() < 0.3) continue; 
            
            HealthData newData = generateHealthData(patient.getId());
            latestData.put(patient.getId(), newData);
            
            boolean shouldAlert = shouldTriggerCriticalAlert(patient.getId(), newData);
            
            for (DataListener listener : listeners) {
                listener.onDataReceived(patient.getId(), newData);
                
                if (shouldAlert) {
                    String alertMessage = generateAlertMessage(newData);
                    listener.onCriticalAlert(patient.getId(), newData, alertMessage);
                    lastCriticalAlert.put(patient.getId(), LocalDateTime.now());
                    consecutiveNormalReadings.put(patient.getId(), 0);
                }
            }
        }
    }
    
    private boolean shouldTriggerCriticalAlert(int patientId, HealthData data) {
        if (!isCriticalData(data)) {
            consecutiveNormalReadings.merge(patientId, 1, Integer::sum);
            return false;
        }
        
        LocalDateTime lastAlert = lastCriticalAlert.get(patientId);
        if (lastAlert != null) {
            LocalDateTime cooldownEnd = lastAlert.plusMinutes(CRITICAL_ALERT_COOLDOWN_MINUTES);
            if (LocalDateTime.now().isBefore(cooldownEnd)) {
                return false;
            }
            
            Integer normalCount = consecutiveNormalReadings.get(patientId);
            if (normalCount == null || normalCount < NORMAL_READINGS_BEFORE_NEXT_ALERT) {
                return false; 
            }
        }
        
        return true;
    }
    
    private boolean isCriticalData(HealthData data) {
        boolean criticalHeartRate = data.getHeartRate() < 50 || data.getHeartRate() > 120;
        boolean criticalTemperature = data.getTemperature() < 35.5 || data.getTemperature() > 38.5;
        
        boolean criticalBloodPressure = false;
        try {
            String[] bpParts = data.getBloodPressure().split("/");
            if (bpParts.length == 2) {
                int systolic = Integer.parseInt(bpParts[0]);
                int diastolic = Integer.parseInt(bpParts[1]);
                criticalBloodPressure = systolic < 80 || systolic > 160 || diastolic < 50 || diastolic > 100;
            }
        } catch (NumberFormatException e) {
        }
        
        return criticalHeartRate || criticalTemperature || criticalBloodPressure;
    }
    
    private String generateAlertMessage(HealthData data) {
        List<String> alerts = new ArrayList<>();
        
        if (data.getHeartRate() < 50) {
            alerts.add("Detak jantung terlalu rendah (" + String.format("%.1f", data.getHeartRate()) + " bpm)");
        } else if (data.getHeartRate() > 120) {
            alerts.add("Detak jantung terlalu tinggi (" + String.format("%.1f", data.getHeartRate()) + " bpm)");
        }
        
        if (data.getTemperature() < 35.5) {
            alerts.add("Suhu tubuh terlalu rendah (" + String.format("%.1f", data.getTemperature()) + "°C)");
        } else if (data.getTemperature() > 38.5) {
            alerts.add("Suhu tubuh terlalu tinggi (" + String.format("%.1f", data.getTemperature()) + "°C)");
        }
        
        try {
            String[] bpParts = data.getBloodPressure().split("/");
            if (bpParts.length == 2) {
                int systolic = Integer.parseInt(bpParts[0]);
                int diastolic = Integer.parseInt(bpParts[1]);
                if (systolic < 80 || diastolic < 50) {
                    alerts.add("Tekanan darah terlalu rendah (" + data.getBloodPressure() + " mmHg)");
                } else if (systolic > 160 || diastolic > 100) {
                    alerts.add("Tekanan darah terlalu tinggi (" + data.getBloodPressure() + " mmHg)");
                }
            }
        } catch (NumberFormatException e) {
        }
        
        return String.join(", ", alerts);
    }
    
    private HealthData generateHealthData(int patientId) {
        HealthData previousData = latestData.get(patientId);
        
        double heartRate;
        double temperature;
        String bloodPressure;
        
        if (previousData != null) {
            heartRate = generateTrendingValue(previousData.getHeartRate(), 65, 85, 5);
            temperature = generateTrendingValue(previousData.getTemperature(), 36.5, 37.2, 0.3);
            bloodPressure = generateTrendingBloodPressure(previousData.getBloodPressure());
        } else {
            heartRate = 65 + random.nextGaussian() * 8;
            temperature = 36.8 + random.nextGaussian() * 0.3;
            bloodPressure = generateBloodPressure();
        }
        
        heartRate = Math.max(45, Math.min(130, heartRate));
        temperature = Math.max(35.0, Math.min(39.0, temperature));
        
        String[] positions = {"Lying", "Sitting", "Standing", "Walking"};
        String position = positions[random.nextInt(positions.length)];
        
        return new HealthData(patientId, heartRate, bloodPressure, temperature, position);
    }
    
    private double generateTrendingValue(double previousValue, double minNormal, double maxNormal, double maxChange) {
        if (random.nextDouble() < 0.8) {
            double change = (random.nextGaussian() * maxChange * 0.3); 
            double newValue = previousValue + change;
            
            if (newValue < minNormal) {
                newValue = minNormal + random.nextDouble() * (maxNormal - minNormal) * 0.3;
            } else if (newValue > maxNormal) {
                newValue = maxNormal - random.nextDouble() * (maxNormal - minNormal) * 0.3;
            }
            
            return newValue;
        } else {
            double change = (random.nextGaussian() * maxChange);
            return previousValue + change;
        }
    }
    
    private String generateTrendingBloodPressure(String previousBP) {
        try {
            String[] parts = previousBP.split("/");
            if (parts.length == 2) {
                int prevSystolic = Integer.parseInt(parts[0]);
                int prevDiastolic = Integer.parseInt(parts[1]);
                
                int systolic = (int) generateTrendingValue(prevSystolic, 110, 130, 8);
                int diastolic = (int) generateTrendingValue(prevDiastolic, 70, 85, 5);
                
                systolic = Math.max(80, Math.min(170, systolic));
                diastolic = Math.max(50, Math.min(110, diastolic));
                
                return systolic + "/" + diastolic;
            }
        } catch (NumberFormatException e) {
        }
        
        return generateBloodPressure();
    }
    
    private String generateBloodPressure() {
        int systolic = 115 + (int)(random.nextGaussian() * 10);
        int diastolic = 75 + (int)(random.nextGaussian() * 5);
        
        systolic = Math.max(80, Math.min(170, systolic));
        diastolic = Math.max(50, Math.min(110, diastolic));
        
        return systolic + "/" + diastolic;
    }
    
    public void addDataListener(DataListener listener) {
        listeners.add(listener);
    }
    
    public void removeDataListener(DataListener listener) {
        listeners.remove(listener);
    }
    
    public HealthData getLatestData(int patientId) {
        return latestData.get(patientId);
    }
    
    public boolean isRunning() {
        return isRunning;
    }
}
