// / File: src/main/java/com/caremonitor/view/components/PatientCard.java
package com.caremonitor.view.components;

import com.caremonitor.model.Patient;
import com.caremonitor.model.HealthData;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.caremonitor.view.theme.UIStyles;
import java.text.SimpleDateFormat;

public class PatientCard extends JPanel {
    private Patient patient;
    private HealthData healthData;
    
    public PatientCard(Patient patient, HealthData healthData) {
        this.patient = patient;
        this.healthData = healthData;
        
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UIStyles.LIGHT_GRAY_229, 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));
        
        initializeComponents();
    }
    
    public Patient getPatient() {
        return patient;
    }
    
    public void updateHealthData(HealthData newHealthData) {
        this.healthData = newHealthData;
        removeAll();
        initializeComponents();
        revalidate();
        repaint();
    }
    
    private void initializeComponents() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        
        JLabel nameLabel = new JLabel(patient.getName());
        nameLabel.setFont(UIStyles.ARIAL_BOLD_16);
        
        JLabel statusLabel = new JLabel();
        statusLabel.setOpaque(true);
        statusLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        statusLabel.setFont(UIStyles.ARIAL_BOLD_12);
        
        if (healthData != null) {
            boolean isCritical = isHealthDataCritical(healthData);
            if (isCritical) {
                statusLabel.setText("Critical");
                statusLabel.setBackground(UIStyles.DANGER_RED);
                statusLabel.setForeground(Color.WHITE);
            } else {
                statusLabel.setText("Normal");
                statusLabel.setBackground(UIStyles.GREEN);
                statusLabel.setForeground(Color.WHITE);
            }
        } else {
            statusLabel.setText("No Data");
            statusLabel.setBackground(UIStyles.LIGHT_GRAY_229);
            statusLabel.setForeground(Color.BLACK);
        }
        
        headerPanel.add(nameLabel, BorderLayout.WEST);
        headerPanel.add(statusLabel, BorderLayout.EAST);
        
        JPanel dataPanel = new JPanel();
        dataPanel.setLayout(new BoxLayout(dataPanel, BoxLayout.Y_AXIS));
        dataPanel.setBackground(Color.WHITE);
        
        JPanel heartRatePanel = createDataItemPanel("Heart Rate", 
            healthData != null ? String.format("%.0f", healthData.getHeartRate()) : "N/A", 
            "bpm");
        
        JPanel bpPanel = createDataItemPanel("Blood Pressure", 
            healthData != null ? healthData.getBloodPressure() : "N/A", 
            "mmHg");
        
        JPanel tempPanel = createDataItemPanel("Temperature", 
            healthData != null ? String.format("%.1f", healthData.getTemperature()) : "N/A", 
            "°C");
        
        JPanel positionPanel = createDataItemPanel("Position", 
            healthData != null ? healthData.getPosition() : "N/A", 
            "");
        
        dataPanel.add(heartRatePanel);
        dataPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        dataPanel.add(bpPanel);
        dataPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        dataPanel.add(tempPanel);
        dataPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        dataPanel.add(positionPanel);
        
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        
        JLabel lastUpdatedLabel = new JLabel();
        if (healthData != null && healthData.getTimestamp() != null) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            lastUpdatedLabel.setText("Last updated: " + dateFormat.format(java.sql.Timestamp.valueOf(healthData.getTimestamp())));
        } else {
            lastUpdatedLabel.setText("No data available");
        }
        lastUpdatedLabel.setFont(UIStyles.ARIAL_ITALIC_12);
        lastUpdatedLabel.setForeground(Color.GRAY);
        
        footerPanel.add(lastUpdatedLabel, BorderLayout.WEST);

        add(headerPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(dataPanel);
        add(Box.createRigidArea(new Dimension(0, 10)));
        add(footerPanel);
    }
    
    private JPanel createDataItemPanel(String label, String value, String unit) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        JLabel labelText = new JLabel(label);
        labelText.setFont(UIStyles.ARIAL_PLAIN_12);
        labelText.setForeground(Color.GRAY);
        labelText.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JPanel valuePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        valuePanel.setBackground(Color.WHITE);
        
        JLabel valueText = new JLabel(value);
        valueText.setFont(UIStyles.ARIAL_BOLD_20);
        
        JLabel unitText = new JLabel(" " + unit);
        unitText.setFont(UIStyles.ARIAL_PLAIN_14);
        unitText.setForeground(Color.GRAY);
        
        valuePanel.add(valueText);
        valuePanel.add(unitText);
        valuePanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        panel.add(labelText);
        panel.add(Box.createRigidArea(new Dimension(0, 5)));
        panel.add(valuePanel);
        
        return panel;
    }
    
    private boolean isHealthDataCritical(HealthData data) {
        if (data.getHeartRate() < 60 || data.getHeartRate() > 100) {
            return true;
        }
        
        if (data.getTemperature() < 36.1 || data.getTemperature() > 37.2) {
            return true;
        }
        
        String[] bpParts = data.getBloodPressure().split("/");
        if (bpParts.length == 2) {
            try {
                int systolic = Integer.parseInt(bpParts[0]);
                int diastolic = Integer.parseInt(bpParts[1]);
                
                if (systolic < 90 || systolic > 140 || diastolic < 60 || diastolic > 90) {
                    return true;
                }
            } catch (NumberFormatException e) {
                return true; 
            }
        }
        
        return false;
    }
}